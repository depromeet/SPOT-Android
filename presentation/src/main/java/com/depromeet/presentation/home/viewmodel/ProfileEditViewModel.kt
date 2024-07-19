package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.entity.response.home.PresignedUrlResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.extension.validateNickName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _team = MutableStateFlow<UiState<List<BaseballTeamResponse>>>(UiState.Loading)
    val team = _team.asStateFlow()

    private val _presignedUrl = MutableStateFlow<UiState<PresignedUrlResponse>>(UiState.Loading)
    val presignedUrl = _presignedUrl.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _nickNameError = MutableStateFlow<NickNameError>(NickNameError.NoError)
    val nickNameError = _nickNameError.asStateFlow()

    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    private val _cheerTeam = MutableStateFlow(0)
    val cheerTeam = _cheerTeam.asStateFlow()

    val changeEnabled = MutableStateFlow(false)

    private var initialNickName: String = ""
    private var initialProfileImage: String = ""
    private var initialCheerTeam: Int = 0

    init {
        viewModelScope.launch {
            combine(
                nickName,
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
        viewModelScope.launch {
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

    fun uploadProfileImage(profileByteArray: ByteArray) {
        val currentState = presignedUrl.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                homeRepository.putProfileImage(currentState.data.presignedUrl, profileByteArray)
                    .onSuccess {

                    }
                    .onFailure {
                        setProfileImage("")
                    }
            }
        }
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

        _nickName.value = name
        _profileImage.value = image
        _cheerTeam.value = cheerTeam

        changeEnabled.value = false
    }

    fun updateNickName(nickName: String) {
        _nickName.value = nickName
        _nickNameError.value = nickName.validateNickName()
    }
}