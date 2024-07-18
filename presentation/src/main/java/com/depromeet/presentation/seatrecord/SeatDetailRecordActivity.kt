package com.depromeet.presentation.seatrecord

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DetailRecordAdapter
import com.depromeet.presentation.seatrecord.uiMapper.ReviewUiData
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatDetailRecordActivity : BaseActivity<ActivitySeatDetailRecordBinding>(
    ActivitySeatDetailRecordBinding::inflate
) {
    companion object {
        const val SEAT_DETAIL_TAG = "SEAT_DETAIL"
    }

    private lateinit var detailRecordAdapter: DetailRecordAdapter
    private val viewModel: SeatDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserver()

    }

    private fun initView() {
        setDetailRecordAdapter()
        getDataExtra { id, reviews ->
            val position = reviews.indexOfFirst { it.id == id }
            if(position != -1){
                binding.rvDetailRecord.scrollToPosition(position)
            }
            viewModel.setReviewData(id, reviews)
        }

    }

    private fun initEvent() {
        with(binding) {
            fabDetailUp.setOnClickListener {
                rvDetailRecord.smoothScrollToPosition(0)
            }
        }
    }

    private fun initObserver() {

        viewModel.uiState.asLiveData().observe(this) {
            detailRecordAdapter.submitList(it)
        }

        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state) moveConfirmationDialog()
        }
    }

    private fun setDetailRecordAdapter() {
        detailRecordAdapter = DetailRecordAdapter()
        binding.rvDetailRecord.adapter = detailRecordAdapter

        detailRecordAdapter.itemMoreClickListener =
            object : DetailRecordAdapter.OnDetailItemClickListener {
                override fun onItemMoreClickListener(item: ReviewUiData) {
                    viewModel.setEditReviewId(item.id)
                    RecordEditDialog.newInstance(SEAT_DETAIL_TAG)
                        .apply { show(supportFragmentManager, this.tag) }
                }
            }
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_DETAIL_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU, lambda = 0)
    private fun getDataExtra(callback: (recordId: Int, recordList: ArrayList<ReviewUiData>) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            callback(
                intent?.getIntExtra(SeatRecordActivity.RECORD_ID, 0) ?: 0,
                intent?.getParcelableArrayListExtra(
                    SeatRecordActivity.RECORD_LIST,
                    ReviewUiData::class.java
                ) ?: arrayListOf()
            )
        } else {
            callback(
                intent?.getIntExtra(SeatRecordActivity.RECORD_ID, 0) ?: 0,
                intent?.getParcelableArrayListExtra(SeatRecordActivity.RECORD_LIST) ?: arrayListOf()
            )
        }
    }

}