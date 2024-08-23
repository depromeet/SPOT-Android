package com.dpm.presentation.seatrecord.viewholder

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemRecordSelectReviewBinding
import com.dpm.presentation.extension.setOnSingleClickListener

class RecordSelectReviewViewHolder(
    private val binding: ItemRecordSelectReviewBinding,
    private val seatViewClick: () -> Unit,
    private val intuitiveViewClick: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() = with(binding) {
        tvSeatView.setOnSingleClickListener {
            seatViewClick()
            vSeatViewDivider.visibility = VISIBLE
            vIntuitiveReviewDivider.visibility = GONE
        }
        tvIntuitiveReview.setOnSingleClickListener {
            intuitiveViewClick()
            vSeatViewDivider.visibility = GONE
            vIntuitiveReviewDivider.visibility = VISIBLE
        }
    }
}

