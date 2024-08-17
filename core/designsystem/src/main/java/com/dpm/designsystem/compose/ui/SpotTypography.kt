package com.dpm.designsystem.compose.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.depromeet.designsystem.R

private val platformTextStyle = PlatformTextStyle(
    includeFontPadding = false
)

private val lineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

@Immutable
data class SpotTypography internal constructor(
    val title01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.3.em,
        fontSize = 28.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val title02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.3.em,
        fontSize = 24.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val title03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.3.em,
        fontSize = 22.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val title04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.3.em,
        fontSize = 20.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val subtitle01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.4.em,
        fontSize = 18.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val subtitle02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.45.em,
        fontSize = 16.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val subtitle03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.45.em,
        fontSize = 15.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val subtitle04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.5.em,
        fontSize = 14.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val body01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
        fontSize = 16.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val body02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
        fontSize = 15.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val body03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
        fontSize = 14.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 12.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 18.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 16.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.em,
        fontSize = 16.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label05: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 15.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label06: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.em,
        fontSize = 15.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label07: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 14.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label08: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.em,
        fontSize = 14.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label09: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 13.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label10: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.em,
        fontSize = 13.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label11: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 12.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label12: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.em,
        fontSize = 12.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val label13: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.em,
        fontSize = 11.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption01: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.4.em,
        fontSize = 13.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption02: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.4.em,
        fontSize = 12.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption03: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.4.em,
        fontSize = 11.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption04: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.4.em,
        fontSize = 11.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption05: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.2.em,
        fontSize = 10.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),
    val caption06: TextStyle = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.2.em,
        fontSize = 10.sp,
        platformStyle = platformTextStyle,
        lineHeightStyle = lineHeightStyle,
    ),

    )

val pretendard = FontFamily(
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal)
)

