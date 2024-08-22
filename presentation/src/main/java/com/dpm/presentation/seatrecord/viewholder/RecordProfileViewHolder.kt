package com.dpm.presentation.seatrecord.viewholder

import android.view.View.GONE
import android.view.View.VISIBLE
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
        csbvHelpInfo.setText("내 기록이 야구팬들에게 도움된 횟수에요!")
        ivRecordHelpInfo.setOnClickListener {
            csbvHelpInfo.visibility = if (csbvHelpInfo.visibility == GONE) VISIBLE else GONE
        }
        csbvHelpInfo.setOnClickListener {
            csbvHelpInfo.visibility = GONE
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
