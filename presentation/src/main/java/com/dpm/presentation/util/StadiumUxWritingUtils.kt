package com.dpm.presentation.util

import com.dpm.domain.entity.response.viewfinder.BASE
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.domain.entity.response.viewfinder.base

/**
 * @UX_Writing : 야구장 구장 화면에서 블록 클릭 후, 블록 별 리뷰 화면의 [n루]•[구역]•[블록]
 *              (휠체어석은 레드/블루 구분)
 *              (익사이팅석, 프리미엄석은 따로 구분)
 * @author : 조관희
 */
fun ResponseBlockReview.ResponseLocation.stadiumBlock(): String {
    return when(stadiumName) {
        "서울 잠실 야구장" -> {
            val base = when (stadiumName.base(blockCode)) {
                BASE.Base1 -> "1루•"
                BASE.Base3 -> "3루•"
                else -> ""
            }
            when(blockCode) {
                in listOf("101w", "102w", "122w", "121w") -> "$base${sectionName}•레드-${blockCode.replace("w", "")}블록"
                in listOf("109w", "114w") -> "$base${sectionName}•블루-${blockCode.replace("w", "")}블록"
                in listOf("exciting1") -> "1루•$sectionName"
                in listOf("exciting3") -> "3루•$sectionName"
                in listOf("premium") -> "$sectionName"
                else -> "$base${sectionName}•${blockCode}블록"
            }
        }
        else -> ""
    }
}

/**
 * @UX_Writing : 야구장 구장 화면에서 블록을 클릭하고, 블록 별 리뷰가 존재하지 않을 때
 * @author : 조관희
 */
fun toEmptyBlock(stadiumId: Int, blockCode: String): String {
    return when(stadiumId) {
        1 -> {
           when (blockCode) {
               in listOf("101w", "102w", "122w", "121w", "109w", "114w") -> "휠체어석 ${blockCode.replace("w", "")}블록"
               in listOf("101w", "102w", "122w", "121w", "109w", "114w") -> "휠체어석 ${blockCode.replace("w", "")}블록"
               in listOf("exciting1") -> "1루 익사이팅석"
               in listOf("exciting3") -> "3루 익사이팅석"
               in listOf("premium") -> "프리미엄석"
               else -> "${blockCode} 블록"
            }
        }
        else -> ""

    }
}

/**
 * @UX_Writing : 블록 별 리뷰 조회 후, 리뷰의 사진 클릭 시 넘어온 화면의 앱바 제목
 * @author : 조관희
 */
fun ResponseBlockReview.ResponseLocation.toTitle(): String {
    return when(stadiumName) {
        "서울 잠실 야구장" -> {
            val base = when (stadiumName.base(blockCode)) {
                BASE.Base1 -> "1루"
                BASE.Base3 -> "3루"
                else -> ""
            }

            when(blockCode) {
                in listOf("101w", "102w", "122w", "121w") -> "$stadiumName $base ${sectionName}-레드"
                in listOf("109w", "114w") -> "$stadiumName $base ${sectionName}-블루"
                in listOf("exciting1") -> "$stadiumName 1루 $sectionName"
                in listOf("exciting3") -> "$stadiumName 3루 $sectionName"
                in listOf("premium") -> "$stadiumName $sectionName"
                else -> "$stadiumName $base $sectionName"
            }
        }
        else -> ""
    }
}

/**
 * @UX_Writing : 블록 별 리뷰의 [블록] [열] [번]
 * @author : 조관희
 */
fun ResponseBlockReview.ResponseReview.toBlockContent(): String {
    var numberString = ""
    if (block.code.isNotEmpty()) {
        numberString += if (block.code in listOf("exciting1", "exciting3", "premium")) {
            ""
        } else {
            "${block.code}블록 "
        }
    }
    if (row.number > 0) numberString += "${row.number}열 "
    if (seat.seatNumber > 0) numberString += "${seat.seatNumber}번"
    return numberString
}

fun ResponseBlockReview.ResponseTopReviewImages.toBlockContent(): String {
    var numberString = ""
    if (blockCode.isNotEmpty()) {
        numberString += if (blockCode in listOf("exciting1", "exciting3", "premium")) {
            ""
        } else {
            "${blockCode}블록 "
        }
    }
    if (rowNumber > 0) numberString += "${rowNumber}열 "
    if (seatNumber > 0) numberString += "${seatNumber}번"
    return numberString
}