package com.depromeet.presentation.login.ui

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSignupTextBinding

class SignupViewPagerAdapter(
    private val texts: List<Triple<String, Int, Int>>
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

    fun bind(text: Triple<String, Int, Int>) {
        binding.tvText.text = SpannableStringBuilder(text.first).apply {
            setSpan(
                ForegroundColorSpan(android.graphics.Color.parseColor("#42D596")),
                text.second,
                text.third + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}