package com.depromeet.presentation.seatReview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemSelectSeatBinding

class SelectSeatAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<SelectSeatAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSelectSeatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvSeatName.text = item
            binding.tvSubName.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
