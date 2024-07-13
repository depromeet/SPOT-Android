package com.depromeet.presentation.seatrecord.mockdata

data class MonthData(
    val month : String, //int?
    val isClicked : Boolean
)

val monthList = listOf<MonthData>(
    MonthData("전체",true),
    MonthData("3월",false),
    MonthData("4월",false),
    MonthData("4월",false),
    MonthData("5월",false),
    MonthData("6월",false),
    MonthData("7월",false),
    MonthData("8월",false),
    MonthData("9월",false),
)