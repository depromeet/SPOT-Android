package com.dpm.presentation.seatrecord.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemRecordProfileBinding
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.extension.loadAndCircleProfile

class RecordProfileViewHolder(
    internal val binding: ItemRecordProfileBinding,
    private val editClick: (ResponseMySeatRecord.MyProfileResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ResponseMySeatRecord.MyProfileResponse) = with(binding) {
        initEvent(data)
        initView(data)
    }

    private fun initView(data: ResponseMySeatRecord.MyProfileResponse) = with(binding) {
        if (data.teamId != null && data.teamId != 0) {
            csbvRecordTitle.setTextPart("${data.teamName}의 Lv.", data.level, " ${data.levelTitle}")
        } else {
            csbvRecordTitle.setTextPart("모두를 응원하는 Lv.", data.level, " ${data.levelTitle}")
        }
        tvRecordNickname.text = data.nickname
        tvRecordCount.text = data.reviewCount.toString()
        ivRecordProfile.loadAndCircleProfile(data.profileImage)

    }

    private fun initEvent(data: ResponseMySeatRecord.MyProfileResponse) = with(binding) {
        ivRecordProfile.setOnClickListener { editClick(data) }
        ivRecordEdit.setOnClickListener { editClick(data) }
    }
}
