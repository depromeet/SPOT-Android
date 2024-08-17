package com.dpm.data.model.response

import com.dpm.domain.entity.response.ExampleListModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("tmi_1")
    val tmi1: String,
    @SerialName("tmi_2")
    val tmi2: String,
    @SerialName("tmi_3")
    val tmi3: String,
) {
    fun toExampleEntity() = ExampleListModel(
        id = id,
        title = title,
        content = content,
    )
}
