package com.depromeet.presentation.seatrecord.test

import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.databinding.ItemRecordProfileBinding
import com.depromeet.presentation.extension.loadAndCircle

class RecordProfileViewHolder(
    internal val binding: ItemRecordProfileBinding,
    private val editClick: (MySeatRecordResponse.MyProfileResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        initEvent(data)
        initView(data)
    }

    private fun initView(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        if (data.teamId != null) {
            "${data.teamName}의 Lv.${data.level} ${data.levelTitle}".also {
                csbvRecordTitle.setText(
                    it
                )
            }
        } else {
            "모두를 응원하는 Lv.${data.level} ${data.levelTitle}".also {
                csbvRecordTitle.setText(
                    it
                )
            }
        }
        tvRecordNickname.text = data.nickname
        tvRecordCount.text = data.reviewCount.toString()
        ivRecordProfile.loadAndCircle(data.profileImage)
    }

    private fun initEvent(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        ivRecordProfile.setOnClickListener { editClick(data) }
        ivRecordEdit.setOnClickListener { editClick(data) }
    }
}
