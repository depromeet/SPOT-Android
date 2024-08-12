package com.depromeet.presentation.seatrecord.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.presentation.databinding.ItemRecordDateBinding
import com.depromeet.presentation.databinding.ItemRecordProfileBinding
import com.depromeet.presentation.databinding.ItemRecordReviewBinding
import timber.log.Timber

enum class RecordViewType {
    PROFILE_ITEM,
    DATE_ITEM,
    RECORD_ITEM
}

sealed class RecordListItem {
    data class Profile(val profile: MySeatRecordResponse.MyProfileResponse) : RecordListItem()
    data class Date(val reviewDates: List<ReviewDateResponse.YearMonths>) : RecordListItem()
    data class Record(val reviews: List<MySeatRecordResponse.ReviewResponse>) : RecordListItem()
}

class SeatRecordAdapter(
    private val reviewClick: (MySeatRecordResponse.ReviewResponse) -> Unit,
    private val reviewEditClick: (Int) -> Unit,
    private val monthClick: (Int) -> Unit,
    private val yearClick: (Int) -> Unit,
    private val profileEditClick: (MySeatRecordResponse.MyProfileResponse) -> Unit,
) : ListAdapter<RecordListItem, RecyclerView.ViewHolder>(
    RecordItemDiffCallback()
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecordListItem.Profile -> RecordViewType.PROFILE_ITEM.ordinal
            is RecordListItem.Date -> RecordViewType.DATE_ITEM.ordinal
            is RecordListItem.Record -> RecordViewType.RECORD_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecordViewType.PROFILE_ITEM.ordinal -> {
                RecordProfileViewHolder(
                    ItemRecordProfileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    editClick = profileEditClick
                )
            }

            RecordViewType.DATE_ITEM.ordinal -> {
                RecordDateViewHolder(
                    ItemRecordDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    monthClick = monthClick,
                    yearClick = yearClick
                )

            }

            RecordViewType.RECORD_ITEM.ordinal -> {
                RecordReviewViewHolder(
                    ItemRecordReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    reviewClick = reviewClick,
                    editClick = reviewEditClick
                )
            }

            else -> throw IllegalArgumentException("옳지 않은 뷰타입")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecordProfileViewHolder -> {
                val item = getItem(position) as? RecordListItem.Profile
                item?.let {
                    holder.bind(it.profile)
                }
            }

            is RecordDateViewHolder -> {
                val item = getItem(position) as? RecordListItem.Date
                item?.let {
                    holder.bind(it.reviewDates)
                }
            }

            is RecordReviewViewHolder -> {
                val item = getItem(position) as? RecordListItem.Record
                item?.let {
                    holder.bind(it.reviews)
                }
            }
        }
    }

    fun updateItemAt(index: Int, newItem: RecordListItem) {
        val newList = currentList.toMutableList()
        if (index >= 0 && index < newList.size) {
            Timber.d("test -> 인덱스 :  $index 새로운 아이템 :  $newItem")
            newList[index] = newItem

            submitList(newList) {
                Timber.d("test -> submitlist 완료")
            }
        } else {
            Timber.d("test -> 인덱스 초과: $index")
        }
    }
}

class RecordItemDiffCallback : DiffUtil.ItemCallback<RecordListItem>() {

    override fun areItemsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {

        return when{
            oldItem is RecordListItem.Record && newItem is RecordListItem.Record ->
                oldItem.reviews == newItem.reviews
            oldItem is RecordListItem.Profile && newItem is RecordListItem.Profile ->
                oldItem.profile == newItem.profile
            oldItem is RecordListItem.Date && newItem is RecordListItem.Date ->
                oldItem.reviewDates == newItem.reviewDates
            else -> false
        }
    }
}



