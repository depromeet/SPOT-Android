package com.depromeet.presentation.mapper

import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.viewfinder.sample.Keyword

fun BlockReviewResponse.KeywordResponse.toKeyword() = Keyword(
    message = content,
    like = count,
    type = if (isPositive) 0 else 1
)

fun BlockReviewResponse.ReviewResponse.ReviewKeywordResponse.toKeyword() = Keyword(
    message =  content,
    like = 0,
    type = if (isPositive) 0 else 1
)