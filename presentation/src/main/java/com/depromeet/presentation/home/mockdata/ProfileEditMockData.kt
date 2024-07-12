package com.depromeet.presentation.home.mockdata

import android.os.Parcelable
import com.depromeet.presentation.R
import com.depromeet.presentation.home.viewmodel.EditUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamData(
    val id: Int = 0,
    val name: String = "",
    val image: Int = 0,
    val isClicked: Boolean = false,
) : Parcelable


fun mockDataProfileEdit(): Flow<EditUiState> = flow {

    val testData = mutableListOf<TeamData>()
    testData.add(TeamData(1, "삼성 라이온즈", R.drawable.ic_lg_team, true))
    testData.add(TeamData(2, "안드 화이팅!!", R.drawable.ic_lg_team, false))
    for (i in 3 until 20) {
        testData.add(TeamData(i, "LG 트윈스", R.drawable.ic_lg_team, false))
    }

    emit(EditUiState("https://picsum.photos/600/400", "노균욱", testData))
}