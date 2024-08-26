package com.dpm.presentation.scrap.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
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
    private val scrapClick: (Int) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val shareClick: (ResponseScrap.ResponseReviewWrapper, Int) -> Unit,
) : ListAdapter<ResponseScrap.ResponseReviewWrapper, ScrapDetailViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.baseReview.id == newItem.baseReview.id },
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
    private val scrapClick: (Int) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val shareClick: (ResponseScrap.ResponseReviewWrapper, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var scrapImageAdapter: ScrapImageAdapter
    private lateinit var indicators : MutableList<ImageView>

    fun bind(item: ResponseScrap.ResponseReviewWrapper) = with(binding) {
        ivProfile.loadAndCircleProfile(item.baseReview.member.profileImage ?: "")
        tvScrapNickname.text = item.baseReview.member.nickname
        tvSectionName.text = item.baseReview.member.nickname
        tvScrapLevel.text = item.baseReview.member.formattedLevel()
        tvSectionName.text = item.baseReview.formattedBlockToSeat()
        ivBackground.loadAndClip(item.baseReview.images[0].url)
        if (item.baseReview.content.isNotEmpty()) {
            tvScrapContent.text = item.baseReview.content
        } else {
            tvScrapContent.visibility = GONE
        }
        scrapImageAdapter = ScrapImageAdapter()
        binding.vpImage.adapter = scrapImageAdapter
        scrapImageAdapter.submitList(item.baseReview.images.map { it.url })
        setupIndicators(scrapImageAdapter.itemCount)
        binding.vpImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
            }
        })
        tvLikeCount.text = item.baseReview.likesCount.toString()


        cvScrapKeyword.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    KeywordFlowRow(
                        keywords = item.baseReview.keywords.map { it.toUiKeyword() },
                        overflowIndex = 2
                    )
                }
            }
        }
        if (item.baseReview.isScrapped) {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_active)
        } else {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
        }

        if (item.baseReview.isLiked) {
            ivLike.load(com.depromeet.designsystem.R.drawable.ic_like_active)
        } else {
            ivLike.load(com.depromeet.designsystem.R.drawable.ic_like_inactive)
        }

        if(item.baseReview.content.isEmpty()){
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
            likeClick(item.baseReview.id)
        }
        ivScrap.setOnSingleClickListener {
            scrapClick(item.baseReview.id)
        }
        ivShare.setOnSingleClickListener {
            shareClick(item, binding.vpImage.currentItem)
        }
    }

    private fun setupIndicators(count: Int) {
        indicators = mutableListOf()
        binding.llIndicator.removeAllViews()
        val context = binding.root.context

        if (count < 2) {
            binding.llIndicator.visibility = GONE
        } else {
            binding.llIndicator.visibility = VISIBLE
            for (i in 0 until count) {
                val indicator = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        6.dpToPx(context), 6.dpToPx(context)
                    ).apply {
                        setMargins(4.dpToPx(context), 0, 4.dpToPx(context), 0)
                    }
                    scaleType = ImageView.ScaleType.FIT_XY
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_unselected))
                }
                indicators.add(indicator)
                binding.llIndicator.addView(indicator)
            }

            updateIndicators(0)
        }
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
                setMargins(4.dpToPx(context), 0, 4.dpToPx(context), 0)
            }

            indicator.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        }
    }

}