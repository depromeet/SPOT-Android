package com.dpm.domain.model.seatrecord

enum class RecordReviewType {
    VIEW,
    FEED
}

fun RecordReviewType.toTypeName(): String {
    return when (this) {
        RecordReviewType.VIEW -> "좌석시야"
        RecordReviewType.FEED -> "직관후기"
    }
}
