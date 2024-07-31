package com.depromeet.designsystem.compose.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.R

@Immutable
data class SpotTypography internal constructor(
    val title01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.3f, TextUnitType.Sp),
        fontSize = 28.sp,
    ),
    val title02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.3f, TextUnitType.Sp),
        fontSize = 24.sp
    ),
    val title03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.3f, TextUnitType.Sp),
        fontSize = 22.sp
    ),
    val title04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.3f, TextUnitType.Sp),
        fontSize = 20.sp
    ),
    val subtitle01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
        fontSize = 18.sp
    ),
    val subtitle02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.45f, TextUnitType.Sp),
        fontSize = 16.sp
    ),
    val subtitle03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.45f, TextUnitType.Sp),
        fontSize = 15.sp
    ),
    val subtitle04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
        fontSize = 14.sp
    ),
    val body01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
        fontSize = 16.sp
    ),
    val body02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
        fontSize = 15.sp
    ),
    val body03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
        fontSize = 14.sp
    ),
    val label01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 12.sp
    ),
    val label02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 18.sp
    ),
    val label03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 16.sp
    ),
    val label04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 16.sp
    ),
    val label05: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 15.sp
    ),
    val label06: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 15.sp
    ),
    val label07: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 14.sp
    ),
    val label08: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 14.sp
    ),
    val label09: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 13.sp
    ),
    val label10: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 13.sp
    ),
    val label11: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 12.sp
    ),
    val label12: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 12.sp
    ),
    val label13: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1f, TextUnitType.Sp),
        fontSize = 11.sp
    ),
    val caption01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
        fontSize = 13.sp
    ),
    val caption02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
        fontSize = 12.sp
    ),
    val caption03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
        fontSize = 11.sp
    ),
    val caption04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
        fontSize = 11.sp
    ),
    val caption05: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = TextUnit(1.2f, TextUnitType.Sp),
        fontSize = 10.sp
    ),
    val caption06: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(1.2f, TextUnitType.Sp),
        fontSize = 10.sp
    ),

    )

val pretendard = FontFamily(
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal)
)

