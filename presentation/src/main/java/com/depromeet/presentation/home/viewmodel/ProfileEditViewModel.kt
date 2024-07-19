package com.depromeet.presentation.home.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.home.ProfileEditRequest
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse
import com.depromeet.domain.entity.response.home.ProfileEditResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.extension.validateNickName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileEvents {
    data class ShowSnackMessage(
        val message: String,
    ) : ProfileEvents()
}

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _events = MutableSharedFlow<ProfileEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<ProfileEvents> = _events.asSharedFlow()

    private val _team = MutableStateFlow<UiState<List<BaseballTeamResponse>>>(UiState.Loading)
    val team = _team.asStateFlow()

    private val _presignedUrl = MutableStateFlow<UiState<PresignedUrlResponse>>(UiState.Loading)
    val presignedUrl = _presignedUrl.asStateFlow()

    private val _profileEdit = MutableStateFlow<UiState<ProfileEditResponse>>(UiState.Loading)
    val profileEdit = _profileEdit.asStateFlow()

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _nickNameError = MutableStateFlow<NickNameError>(NickNameError.NoError)
    val nickNameError = _nickNameError.asStateFlow()

    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    private val _cheerTeam = MutableStateFlow(0)
    val cheerTeam = _cheerTeam.asStateFlow()

    val changeEnabled = MutableStateFlow(false)
    private var profileImageJob: Job? = null
    private var profilePresignedJob: Job? = null

    private var initialNickName: String = ""
    private var initialProfileImage: String = ""
    private var initialCheerTeam: Int = 0

    init {
        viewModelScope.launch {
            combine(
                nickname,
                profileImage,
                cheerTeam,
                nickNameError
            ) { nickName, profileImage, cheerTeam, nickNameError ->
                (nickName != initialNickName || profileImage != initialProfileImage
                        || cheerTeam != initialCheerTeam) && nickNameError == NickNameError.NoError
            }.collect { isChanged ->
                changeEnabled.value = isChanged
            }
        }
    }

    fun getBaseballTeam() {
        viewModelScope.launch {
            homeRepository.getBaseballTeam()
                .onSuccess { teams ->
                    val updatedTeams = teams.map { team ->
                        team.copy(isClicked = team.id == cheerTeam.value)
                    }
                    _team.value = UiState.Success(updatedTeams)
                }.onFailure {
                    _team.value = UiState.Failure(it.message ?: "실패")
                }
        }
    }

    fun setProfileImage(uri: String) {
        _profileImage.value = uri
    }

    fun setProfileImagePresigned(
        profileByteArray: ByteArray,
        fileExtension: String,
        memberId: Int,
    ) {
        profilePresignedJob = viewModelScope.launch {
            homeRepository.postProfileImagePresigned(fileExtension, memberId)
                .onSuccess { presignedUrl ->
                    _presignedUrl.value = UiState.Success(presignedUrl)
                    uploadProfileImage(profileByteArray)
                }
                .onFailure {
                    _presignedUrl.value = UiState.Failure(it.message ?: "실패")
                }
        }
    }

    private fun uploadProfileImage(profileByteArray: ByteArray) {
        val currentState = presignedUrl.value
        if (currentState is UiState.Success) {
            profileImageJob = viewModelScope.launch {
                homeRepository.putProfileImage(
                    currentState.data.presignedUrl,
                    profileByteArray
                )
                    .onSuccess {

                    }
                    .onFailure {
                        _events.emit(ProfileEvents.ShowSnackMessage("프로필 이미지 업로드에 실패하였습니다\n다시 시도해주세요~"))
                        setProfileImage(initialProfileImage)
                    }
            }
        }
    }

    fun uploadProfileEdit() {
        viewModelScope.launch {
            if ((profileImageJob?.isActive == true) || (profilePresignedJob?.isActive == true)) {
                profileImageJob?.join()
                profilePresignedJob?.join()
            }

            homeRepository.putProfileEdit(
                ProfileEditRequest(
                    url = getPresignedUrlOrNull(),
                    nickname = nickname.value,
                    teamId = cheerTeam.value
                ), memberId = 1
                /** memberId ->추후에 메인화면 api 나오면 연동해서 stateflow 업데이트*/
            )
                .onSuccess {
                    _profileEdit.value = UiState.Success(it)
                }
                .onFailure {
                    _events.emit(ProfileEvents.ShowSnackMessage("프로필 업데이트에 실패하였습니다 다시 시도해주세요"))
                }
        }
    }

    private fun getPresignedUrlOrNull(): String? {
        val currentState = presignedUrl.value
        return if (currentState is UiState.Success) {
            removeQueryParameters(currentState.data.presignedUrl)
        } else null
    }

    private fun removeQueryParameters(url: String): String {
        val uri = Uri.parse(url)
        return uri.buildUpon().clearQuery().build().toString()
    }


    fun setClickedBaseballTeam(id: Int) {
        val currentState = team.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map { team ->
                team.copy(isClicked = team.id == id)
            }
            _team.value = UiState.Success(updatedList)
            _cheerTeam.value = id
        }
    }

    fun deleteCheerTeam() {
        val currentState = team.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map { team ->
                team.copy(isClicked = false)
            }
            _team.value = UiState.Success(updatedList)
            _cheerTeam.value = 0
        }
    }

    fun initProfile(name: String, image: String, cheerTeam: Int) {
        initialNickName = name
        initialProfileImage = image
        initialCheerTeam = cheerTeam

        _nickname.value = name
        _profileImage.value = image
        _cheerTeam.value = cheerTeam

        changeEnabled.value = false
    }

    fun updateNickName(nickName: String) {
        _nickname.value = nickName
        _nickNameError.value = nickName.validateNickName()

        if (_nickNameError.value == NickNameError.NoError && initialNickName != nickname.value) {
            checkNickNameAvailability(nickName)
        }
    }

    private fun checkNickNameAvailability(nickName: String) {
        viewModelScope.launch {
            homeRepository.getDuplicateNickname(nickName)
                .onSuccess {
                    _nickNameError.value = NickNameError.NoError
                }
                .onFailure {
                    _nickNameError.value = NickNameError.DuplicateError
                }
        }
    }
}