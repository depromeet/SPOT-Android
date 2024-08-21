package com.dpm.domain.entity.response.viewfinder

data class ResponseStadium(
    val id: Int,
    val name: String = "",
    val homeTeams: List<ResponseStadiums.ResponseHomeTeams> = emptyList(),
    val thumbnail: String = "",
    val stadiumUrl: String = "",
    val sections: List<Section>,
    val blockTags: List<ResponseBlockTags> = emptyList()
) {
    data class ResponseBlockTags(
        val id: Int,
        val name: String = "",
        val blockCodes: List<String> = emptyList(),
        val description: String = "",
        val isActive: Boolean = false
    )
}
