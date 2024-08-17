package com.dpm.presentation.mapper

import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.dpm.presentation.viewfinder.sample.Keyword

fun ResponseBlockReview.ResponseKeyword.toKeyword() = Keyword(
    message = content,
    like = count,
    type = if (isPositive) 0 else 1
)

fun ResponseBlockReview.ResponseReview.ResponseReviewKeyword.toKeyword() = Keyword(
    message =  content,
    like = 0,
    type = if (isPositive) 0 else 1
)