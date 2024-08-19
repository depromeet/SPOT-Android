package com.dpm.presentation.viewfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.presentation.databinding.ItemBlockRecommendBinding
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.viewholder.StadiumSectionRecommendViewHolder


data class Recommend(
    val id: Int,
    val title: String,
    val blocks: List<String>,
    val isActive: Boolean
)

val recommend = listOf(
    Recommend(1, "\uD83E\uDD1D 타팀 팬과 함께", listOf("316", "317", "318", "319"), false),
    Recommend(
        2,
        "\uD83D\uDC40 선수들과 가까이",
        listOf("107", "108", "109", "114", "115", "116", "exciting1", "exciting3"),
        false
    ),
    Recommend(
        3,
        "\uD83C\uDF54 음식 먹기 편한",
        listOf("110", "111", "112", "113", "212", "213", "214", "215"),
        false
    ),
    Recommend(
        4,
        "\uD83D\uDEA9 서서 응원하기",
        listOf("205", "206", "207", "208", "219", "220", "221", "222", "306", "307", "328", "329"),
        false
    ),
    Recommend(
        5,
        "\uD83D\uDCE3 응원 배우기",
        listOf("205", "206", "207", "208", "219", "220", "221", "222", "306", "307", "328", "329"),
        false
    ),
    Recommend(
        6,
        "\uD83C\uDF24️ 햇빛 피하기",
        listOf("205", "206", "207", "208", "219", "220", "221", "222", "306", "307", "328", "329"),
        false
    ),
    Recommend(
        7,
        "\uD83C\uDF1F 추천 외야석",
        listOf("205", "206", "207", "208", "219", "220", "221", "222", "306", "307", "328", "329"),
        false
    ),
)

class StadiumSectionRecommendAdapter : ListAdapter<Recommend, StadiumSectionRecommendViewHolder>(
    ItemDiffCallback<Recommend>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentsTheSame = { old, new -> old == new }
    )
) {

    interface OnItemFilterClickListener {
        fun onItemFilterClick(recommend: Recommend)
    }

    var itemFilterClickListener: OnItemFilterClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StadiumSectionRecommendViewHolder {
        return StadiumSectionRecommendViewHolder(
            ItemBlockRecommendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StadiumSectionRecommendViewHolder, position: Int) {
        holder.bind(currentList[position], position == currentList.size - 1)
        holder.itemView.setOnClickListener {
            itemFilterClickListener?.onItemFilterClick(currentList[position])
        }
    }
}
