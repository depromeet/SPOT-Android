package com.depromeet.presentation.login.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSignupTextBinding

class SignupViewPagerAdapter(
    private val texts: List<String>
) : RecyclerView.Adapter<TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_signup_text, parent, false)
        return TextViewHolder(ItemSignupTextBinding.bind(view))
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(texts[position])
    }

    override fun getItemCount(): Int = texts.size
}

class TextViewHolder(
    private val binding: ItemSignupTextBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.tvText.text = text
    }
}