package com.depromeet.data.model.request

import com.depromeet.domain.entity.request.RequestUploadUrlModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestUploadUrlDto(
    @SerialName("fileExtension")
    val fileExtension: String,
)

fun RequestUploadUrlModel.toRequestUploadUrl(): RequestUploadUrlDto {
    return RequestUploadUrlDto(fileExtension)
}
