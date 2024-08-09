package com.depromeet.designsystem.compose.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.depromeet.designsystem.R

@Immutable
data class SpotColor internal constructor(
    val transferBlack01: Color = Color(0x4D000000),
    val transferBlack02: Color = Color(0x99000000),
    val transferBlack03: Color = Color(0xE6212124),

    val black: Color = Color(0xFF000000),

    val gray00: Color = Color(0xFFFFFFFF),
    val gray50: Color = Color(0xFFF7F8FA),
    val gray100: Color = Color(0xFFF2F3F6),
    val gray200: Color = Color(0xFFEAEBEE),
    val gray300: Color = Color(0xFFDCDEE3),
    val gray400: Color = Color(0xFFD1D3D8),
    val gray500: Color = Color(0xFFADB1BA),
    val gray600: Color = Color(0xFF868B94),
    val gray700: Color = Color(0xFF4D5159),
    val gray800: Color = Color(0xFF393A3F),
    val gray900: Color = Color(0xFF212124),

    val green50: Color = Color(0xFFECFBF5),
    val green100: Color = Color(0xFFC4F2DE),
    val green200: Color = Color(0xFFA8ECCF),
    val green300: Color = Color(0xFF80E3B9),
    val green400: Color = Color(0xFF68DDAB),
    val green500: Color = Color(0xFF42D596),
    val green600: Color = Color(0xFF3CC289),
    val green700: Color = Color(0xFF2F976B),
    val green800: Color = Color(0xFF247553),
    val green900: Color = Color(0xFF1C593F),

    val orange50: Color = Color(0xFFFFF1EF),
    val orange100: Color = Color(0xFFFED4CF),
    val orange200: Color = Color(0xFFFEC0B7),
    val orange300: Color = Color(0xFFFEA396),
    val orange400: Color = Color(0xFFFD9182),
    val orange500: Color = Color(0xFFFD7563),
    val orange600: Color = Color(0xFFE66A5A),
    val orange700: Color = Color(0xFFB45346),
    val orange800: Color = Color(0xFF8B4036),
    val orange900: Color = Color(0xFF6A312A),

    val kakaoSymbol: Color = Color(0xD9000000),
    val kakaoButton: Color = Color(0xFFFEE500),

    val foregroundHeading: Color = gray900,
    val foregroundBodySubtitle: Color = gray800,
    val foregroundBodySebtext: Color = gray700,
    val foregroundCaption: Color = gray600,
    val foregroundDisabled: Color = gray400,
    val foregroundWhite: Color = gray00,

    val backgroundWhite: Color = Color(0xFFFFFFFF),
    val backgroundTertiary: Color = gray50,
    val backgroundSecondary: Color = gray100,
    val backgroundPrimary: Color = gray200,
    val backgroundPositive: Color = green50,
    val backgroundNegative: Color = orange50,

    val strokeTertiary: Color = gray200,
    val strokeSecondary: Color = gray300,
    val strokePrimary: Color = gray400,
    val strokePositivePrimary: Color = green500,
    val strokePositiveSecondary: Color = green300,
    val strokeNegative: Color = orange300,

    val actionEnabled: Color = green500,
    val actionHover: Color = green700,
    val actionDisabled: Color = green100,

    val errorPrimary: Color = orange500,
    val errorSecondary: Color = orange200,
    val errorTertiary: Color = orange50,

    val seatOutfieldGreen: Color = Color(R.color.color_seat_outfield_green),
    val seatTable: Color = Color(R.color.color_seat_table),
    val seatWheelchair: Color = Color(R.color.color_seat_wheelchair),
    val seatNavy: Color = Color(R.color.color_seat_navy),
    val seatRed: Color = Color(R.color.color_seat_red),
    val seatBlue: Color = Color(R.color.color_seat_blue),
    val seatExciting: Color = Color(R.color.color_seat_exciting),
    val seatPremium: Color = Color(R.color.color_seat_premium),
    val seatOrange: Color = Color(R.color.color_seat_orange),

    val spotGreen2CD7A6: Color = Color(0xFF2CD7A6),
    val spotGreen5DD281: Color = Color(0xFF5DD281)
)