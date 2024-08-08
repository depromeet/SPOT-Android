package com.depromeet.presentation.viewfinder.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.viewfinder.ResponseStadiums
import com.depromeet.presentation.databinding.ItemStadiumSelectionBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.util.ItemDiffCallback
import com.depromeet.presentation.viewfinder.viewholder.StadiumSelectionViewHolder

class StadiumSelectionAdapter : ListAdapter<ResponseStadiums, StadiumSelectionViewHolder>(
    ItemDiffCallback<ResponseStadiums>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentsTheSame = { old, new -> old == new }
    )
) {
    interface OnItemStadiumClickListener {
        fun onItemStadiumClick(stadiums: ResponseStadiums)
    }

    var itemStadiumClickListener: OnItemStadiumClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StadiumSelectionViewHolder {
        return StadiumSelectionViewHolder(
            ItemStadiumSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StadiumSelectionViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            itemStadiumClickListener?.onItemStadiumClick(currentList[position])
        }
    }
}

internal class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (column % 2 == 0) {
             outRect.apply {
                 right = spacing
                 bottom = 20.dpToPx(view.context)
             }
        } else {
            outRect.apply {
                left = spacing
                bottom = 20.dpToPx(view.context)
            }
        }



//        if (position >= 0) {
//            outRect.apply {
//                left = spacing - column * spacing / spanCount
//                right = (column + 1) * spacing / spanCount
//                if (position < spanCount) top = spacing
//                bottom = spacing
//            }
//        } else {
//            outRect.apply {
//                left = 0
//                right = 0
//                top = 0
//                bottom = 0
//            }
//        }
    }
}