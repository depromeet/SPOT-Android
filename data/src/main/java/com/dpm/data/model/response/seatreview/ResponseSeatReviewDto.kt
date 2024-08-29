package com.dpm.data.model.response.seatreview

import com.dpm.domain.entity.response.seatreview.ResponseSeatReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSeatReviewDto(
    @SerialName("id")
    val id: Int,
    @SerialName("member")
    val member: Member,
    @SerialName("stadium")
    val stadium: Stadium,
    @SerialName("section")
    val section: Section,
    @SerialName("block")
    val block: Block,
    @SerialName("row")
    val row: Row,
    @SerialName("seat")
    val seat: Seat?,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("content")
    val content: String?,
    @SerialName("images")
    val images: List<Image>,
    @SerialName("keywords")
    val keywords: List<Keyword>,
    @SerialName("likesCount")
    val likesCount: Int,
    @SerialName("scrapsCount")
    val scrapsCount: Int,
    @SerialName("reviewType")
    val reviewType: String,
) {

    @Serializable
    data class Member(
        @SerialName("profileImage")
        val profileImage: String,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("level")
        val level: Int,
    )

    @Serializable
    data class Stadium(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class Section(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("alias")
        val alias: String?,
    )

    @Serializable
    data class Block(
        @SerialName("id")
        val id: Int,
        @SerialName("code")
        val code: String,
    )

    @Serializable
    data class Row(
        @SerialName("id")
        val id: Int,
        @SerialName("number")
        val number: Int,
    )

    @Serializable
    data class Seat(
        @SerialName("id")
        val id: Int,
        @SerialName("seatNumber")
        val seatNumber: Int,
    )

    @Serializable
    data class Image(
        @SerialName("id")
        val id: Int,
        @SerialName("url")
        val url: String,
    )

    @Serializable
    data class Keyword(
        @SerialName("id")
        val id: Int,
        @SerialName("content")
        val content: String,
        @SerialName("isPositive")
        val isPositive: Boolean,
    )

    fun toResponseSeatReview(): ResponseSeatReview {
        return ResponseSeatReview(
            id = id,
        )
    }
}
