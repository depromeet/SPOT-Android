package com.dpm.domain.entity.request.home

class RequestScrap(
    val stadiumId: Int? = null,
    val months: List<Int>? = null,
    val good: List<String>? = null,
    val bad: List<String>? = null,
)