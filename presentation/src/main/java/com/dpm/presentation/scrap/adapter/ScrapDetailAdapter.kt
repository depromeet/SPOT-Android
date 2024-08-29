package com.dpm.presentation.scrap.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemScrapDetailBinding
import com.dpm.domain.entity.response.home.ResponseScrap
import com.dpm.presentation.extension.dpToPx
import com.dpm.presentation.extension.loadAndCircleProfile
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatrecord.uiMapper.toUiKeyword
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.compose.KeywordFlowRow

class ScrapDetailAdapter(
    private val scrapClick: (Int, Boolean) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val shareClick: (ResponseScrap.ResponseBaseReview, Int) -> Unit,
) : ListAdapter<ResponseScrap.ResponseBaseReview, ScrapDetailViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapDetailViewHolder {
        return ScrapDetailViewHolder(
            ItemScrapDetailBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), scrapClick, likeClick, shareClick
        )
    }

    override fun onBindViewHolder(holder: ScrapDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ScrapDetailViewHolder(
    private val binding: ItemScrapDetailBinding,
    private val scrapClick: (Int, Boolean) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val shareClick: (ResponseScrap.ResponseBaseReview, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var scrapImageAdapter: ScrapImageAdapter
    private lateinit var indicators: MutableList<ImageView>

    fun bind(item: ResponseScrap.ResponseBaseReview) = with(binding) {
        ivProfile.loadAndCircleProfile(item.member.profileImage ?: "")
        tvScrapNickname.text = item.member.nickname
        tvSectionName.text = item.member.nickname
        tvScrapLevel.text = item.member.formattedLevel()
        tvSectionName.text = item.formattedBlockToSeat()
        tvLikeCount.text = item.likesCount.toString()
        if (item.content.isNotEmpty()) {
            tvScrapContent.text = item.content
        } else {
            tvScrapContent.visibility = GONE
        }
        initScrapImageAdapter(item)


        cvScrapKeyword.apply {
            setContent {
                MaterialTheme {
                    KeywordFlowRow(
                        keywords = item.keywords.map { it.toUiKeyword() },
                        overflowIndex = 2
                    )
                }
            }
        }
        if (item.isScrapped) {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_active)
            ivScrap.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    com.depromeet.designsystem.R.color.color_action_enabled
                )
            )
        } else {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
            ivScrap.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    com.depromeet.designsystem.R.color.color_foreground_white
                )
            )
        }


        if (item.isLiked) {
            ivLike.load(com.depromeet.designsystem.R.drawable.ic_like_active)
            tvLikeCount.setTextColor(binding.root.context.getColor(com.depromeet.designsystem.R.color.color_action_enabled))
        } else {
            ivLike.load(com.depromeet.designsystem.R.drawable.ic_like_inactive)
            tvLikeCount.setTextColor(binding.root.context.getColor(com.depromeet.designsystem.R.color.color_foreground_white))
        }

        if (item.content.isEmpty()) {
            tvMore.visibility = INVISIBLE
        }
        tvScrapContent.post {
            if (tvScrapContent.layout != null) {
                if (!(tvScrapContent.layout.getEllipsisCount(0) > 0)) {
                    tvMore.visibility = INVISIBLE
                }
            }
        }


        //클릭 처리
        root.setOnClickListener {
            if (tvScrapContent.maxLines == Integer.MAX_VALUE) {
                tvMore.visibility = VISIBLE
                tvScrapContent.maxLines = 1
            }
        }
        vpImage.setOnClickListener {
            if (tvScrapContent.maxLines == Integer.MAX_VALUE) {
                tvMore.visibility = VISIBLE
                tvScrapContent.maxLines = 1
            }
        }
        tvMore.setOnClickListener {
            if (tvScrapContent.maxLines == 1) {
                tvMore.visibility = INVISIBLE
                tvScrapContent.maxLines = Integer.MAX_VALUE
            }
        }

        tvScrapContent.setOnClickListener {
            if (tvScrapContent.maxLines == Integer.MAX_VALUE) {
                tvMore.visibility = VISIBLE
                tvScrapContent.maxLines = 1
            }
        }
        ivLike.setOnSingleClickListener {
            if (!item.isLiked) {
                lottieLike.playAnimation()
                ivLike.load(com.depromeet.designsystem.R.drawable.ic_like_active)
                lottieLike.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                        super.onAnimationEnd(animation, isReverse)
                        likeClick(item.id)
                    }
                })
            } else {
                likeClick(item.id)
            }

        }
        ivScrap.setOnSingleClickListener {
            scrapClick(item.id, item.isScrapped)
        }
        ivShare.setOnSingleClickListener {
            shareClick(item, binding.vpImage.currentItem)
        }
    }

    private fun initScrapImageAdapter(item: ResponseScrap.ResponseBaseReview) {
        if (!::scrapImageAdapter.isInitialized) {
            scrapImageAdapter = ScrapImageAdapter()
            binding.vpImage.adapter = scrapImageAdapter
            scrapImageAdapter.submitList(item.images.map { it.url })
            setupIndicators(scrapImageAdapter.itemCount)
            binding.vpImage.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateIndicators(position)
                    if (position >= 0 && position < item.images.size) {
                        binding.ivBackground.loadAndClip(item.images[position].url)
                    } else {
                        binding.ivBackground.loadAndClip(item.images[0].url)
                    }
                }
            })
        }
    }

    private fun setupIndicators(count: Int) {
        indicators = mutableListOf()
        binding.llIndicator.removeAllViews()
        val context = binding.root.context

        for (i in 0 until count) {
            val indicator = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    6.dpToPx(context), 6.dpToPx(context)
                ).apply {
                    setMargins(2.dpToPx(context), 0, 2.dpToPx(context), 0)
                }
                scaleType = ImageView.ScaleType.FIT_XY
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_unselected
                    )
                )
            }
            indicators.add(indicator)
            binding.llIndicator.addView(indicator)
        }

        updateIndicators(0)
    }

    private fun updateIndicators(selectedPosition: Int) {
        val context = binding.root.context
        for ((index, indicator) in indicators.withIndex()) {
            val drawableResId = if (index == selectedPosition) {
                R.drawable.indicator_selected
            } else {
                R.drawable.indicator_unselected
            }

            val size = if (index == selectedPosition) {
                15.dpToPx(context)
            } else {
                6.dpToPx(context)
            }

            indicator.layoutParams = LinearLayout.LayoutParams(size, 6.dpToPx(context)).apply {
                setMargins(2.dpToPx(context), 0, 2.dpToPx(context), 0)
            }

            indicator.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        }
    }

}