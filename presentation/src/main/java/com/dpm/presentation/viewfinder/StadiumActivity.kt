package com.dpm.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.designsystem.R
import com.depromeet.presentation.databinding.ActivityStadiumBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotTeamLabel
import com.dpm.designsystem.extension.dpToPx
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.domain.entity.response.viewfinder.Section
import com.dpm.domain.preference.SharedPreference
import com.dpm.presentation.extension.setMargins
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.util.SpannableStringUtils
import com.dpm.presentation.util.Utils
import com.dpm.presentation.viewfinder.adapter.StadiumSectionAdapter
import com.dpm.presentation.viewfinder.adapter.StadiumSectionRecommendAdapter
import com.dpm.presentation.viewfinder.viewmodel.StadiumViewModel
import com.dpm.presentation.viewfinder.web.AndroidBridge
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

enum class FilterType(
    val ranges: List<Pair<Int, Int>>
) {
    Type1(listOf(Pair(15, 21), Pair(26, 37))), // 1
    Type2(listOf(Pair(0, 5), Pair(9, 16), Pair(26, 29), Pair(33, 42))), // 2
    Type3(listOf(Pair(10, 27))), // 3
    Type4(listOf(Pair(24, 36), Pair(40, 45), Pair(46, 51))), // 4
    Type5(listOf(Pair(20, 24), Pair(27, 53))), // 5
    Type6(listOf(Pair(9, 16), Pair(21, 26), Pair(30, 37), Pair(42, 47))), // 6
    Type7(listOf(Pair(41, 70))); // 7
}

@AndroidEntryPoint
class StadiumActivity : BaseActivity<ActivityStadiumBinding>({
    ActivityStadiumBinding.inflate(it)
}) {
    companion object {
        private const val BASE_URL = "file:///android_asset/web/"
        private const val ENCODING_UTF8 = "UTF-8"
        private const val MIME_TYPE_TEXT_HTML = "text/html"

        private const val BOTTOM_SHEET_PEEK_HEIGHT = 76 // 바텀 시트 peek 높이
        private const val BUTTON_BOTTOM_MARGIN = 24 // 버튼과 바텀 시트 사이 간격

        private const val TIP_CONTAINER_HORIZONTAL_MARGIN = 16
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    private val viewModel: StadiumViewModel by viewModels()
    private val utils: Utils by lazy {
        Utils(this)
    }
    private lateinit var stadiumSectionRecommendAdapter: StadiumSectionRecommendAdapter
    private lateinit var stadiumSectionAdapter: StadiumSectionAdapter
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserve()
    }


    private fun initView() {
        initStatusBar()
        getStadiumIdExtra()
        configureWebViewSetting()
        initRecyclerViewAdapter()
        initBottomSheetBehavior()
    }

    private fun initEvent() {
        interactionWebView()
        onClickBack()
        onClickClose()
        onClickRefresh()
        onClickTipContainer()
    }

    private fun initObserve() {
        viewModel.stadium.observe(this) { stadium ->
            when (stadium) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(stadium.msg)
                is UiState.Loading -> {
                    startShimmer()
                }

                is UiState.Success -> {
                    viewModel.stadiumId = stadium.data.id
                    with(binding) {
                        tvStadiumTitle.text = stadium.data.name
                        ivStadium.load(stadium.data.thumbnail) {
                            transformations(RoundedCornersTransformation(10f))
                        }
                        llStadiumTeamLabel.removeAllViews()
                        stadium.data.homeTeams.forEach { homeTeam ->
                            llStadiumTeamLabel.addView(
                                SpotTeamLabel(this@StadiumActivity).apply {
                                    teamType = homeTeam.alias
                                }
                            )
                        }
                    }
                    viewModel.downloadFileFromServer(stadium.data.stadiumUrl)
                    stadiumSectionRecommendAdapter.submitList(stadium.data.blockTags)
                    stadiumSectionAdapter.submitList(stadium.data.sections)
                    viewModel.setBlockFilters(stadium.data.blockTags)
                    viewModel.setSections(stadium.data.sections)
                }

                else -> Unit
            }
        }
        viewModel.htmlBody.observe(this) { uiState ->
            when (uiState) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(uiState.msg)
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    loadWebView(uiState.data)
                }

                else -> Unit
            }
        }
        viewModel.blockFilters.observe(this) {
            stadiumSectionRecommendAdapter.submitList(it)
        }
        viewModel.sections.observe(this) {
            stadiumSectionAdapter.submitList(it)
        }
    }

    private fun getStadiumIdExtra() {
        intent?.getIntExtra(StadiumSelectionActivity.STADIUM_EXTRA_ID, 0)?.let { stadiumId ->
            viewModel.getStadium(stadiumId)
        } ?: return
    }

    private fun configureWebViewSetting() {
        binding.wvStadium.settings.apply {
            builtInZoomControls = true
            loadWithOverviewMode = true
            displayZoomControls = false
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
        }
        binding.wvStadium.addJavascriptInterface(
            AndroidBridge { sectionId ->
                val id = sectionId.split("_").last()
                if (id.isEmpty()) return@AndroidBridge

                startToStadiumDetailActivity(id)
            },
            AndroidBridge.JAVASCRIPT_OBJ
        )
    }

    private fun initRecyclerViewAdapter() {
        adaptRecommendFilter()
        adaptSection()
    }

    private fun adaptRecommendFilter() {
        stadiumSectionRecommendAdapter = StadiumSectionRecommendAdapter()
        binding.rcvRecommend.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = stadiumSectionRecommendAdapter
        }

        stadiumSectionRecommendAdapter.itemFilterClickListener =
            object : StadiumSectionRecommendAdapter.OnItemFilterClickListener {
                override fun onItemFilterClick(recommend: ResponseStadium.ResponseBlockTags) {
                    viewModel.updateBlockFilter(recommend)
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    setSectionText(
                        getString(com.depromeet.presentation.R.string.viewfinder_find_section_description),
                        R.color.color_foreground_caption
                    )
                    viewModel.refreshSections()
                    if (recommend.isActive) {
                        binding.clTipContainer.visibility = GONE
                        binding.btnRefresh.visibility = GONE
                        refresh()
                    } else {
                        binding.clTipContainer.visibility = VISIBLE
                        binding.btnRefresh.visibility = VISIBLE
                        filterByBlocks(recommend.blockCodes)
                        when (recommend.id) {
                            1 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type1.ranges
                            )

                            2 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type2.ranges
                            )

                            3 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type3.ranges
                            )

                            4 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type4.ranges
                            )

                            5 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type5.ranges
                            )

                            6 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type6.ranges
                            )

                            7 -> setTipContainerByFilter(
                                recommend.description,
                                FilterType.Type7.ranges
                            )
                        }
                    }
                }
            }
    }

    private fun adaptSection() {
        stadiumSectionAdapter = StadiumSectionAdapter()

        binding.rcvSection.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = stadiumSectionAdapter
            itemAnimator = null
        }

        stadiumSectionAdapter.itemSectionClickListener =
            object : StadiumSectionAdapter.OnItemSectionClickListener {
                override fun onItemSectionClick(section: Section) {
                    binding.clTipContainer.visibility = GONE
                    viewModel.updateSections(section)
                    viewModel.refreshFilter()
                    if (section.isActive) {
                        binding.btnRefresh.visibility = GONE
                        setSectionText(
                            getString(com.depromeet.presentation.R.string.viewfinder_find_section_description),
                            R.color.color_foreground_caption
                        )
                        refresh()
                    } else {
                        binding.btnRefresh.visibility = VISIBLE
                        setSectionText(section.name, R.color.color_action_enabled)
                        when (section.id) {
                            1 -> filterBySection("premium")
                            2 -> filterBySection("table")
                            4 -> filterBySection("blue")
                            5 -> filterBySection("orange")
                            6 -> filterBySection("red")
                            7 -> filterBySection("navy")
                            8 -> filterBySection("exciting")
                            9 -> filterBySection("green")
                            10 -> filterBySection("wheelchair")
                        }
                    }
                }
            }
    }

    private fun setSectionText(text: String, @ColorRes color: Int) {
        binding.tvFindSectionDescription.text = text
        binding.tvFindSectionDescription.setTextColor(
            ContextCompat.getColor(
                this@StadiumActivity,
                color
            )
        )
    }

    private fun initBottomSheetBehavior() {
        behavior = BottomSheetBehavior.from(binding.clBottomSheet)
        behavior.isDraggable = false
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.btnRefresh.setMargins(
                    0, 0, 0,
                    (((binding.clBottomSheet.height / resources.displayMetrics.density) - BOTTOM_SHEET_PEEK_HEIGHT) * slideOffset).dpToPx(
                        this@StadiumActivity
                    )
                            + BOTTOM_SHEET_PEEK_HEIGHT.dpToPx(this@StadiumActivity)
                            + BUTTON_BOTTOM_MARGIN.dpToPx(this@StadiumActivity)
                )
                binding.clTipContainer.setMargins(
                    TIP_CONTAINER_HORIZONTAL_MARGIN.dpToPx(this@StadiumActivity),
                    0,
                    TIP_CONTAINER_HORIZONTAL_MARGIN.dpToPx(this@StadiumActivity),
                    (((binding.clBottomSheet.height / resources.displayMetrics.density) - BOTTOM_SHEET_PEEK_HEIGHT) * slideOffset).dpToPx(
                        this@StadiumActivity
                    )
                            + BOTTOM_SHEET_PEEK_HEIGHT.dpToPx(this@StadiumActivity)
                            + BUTTON_BOTTOM_MARGIN.dpToPx(this@StadiumActivity)
                )
            }
        })
    }

    private fun initTipContainer() {
        initTipContainerSpanColor(binding.tvZoomDescription.text.toString(), 4, 12)
        if (sharedPreference.isFirstTime) {
            binding.clTipContainer.visibility = VISIBLE
            sharedPreference.isFirstTime = false
        } else {
            binding.clTipContainer.visibility = GONE
        }
    }

    private fun initStatusBar() {
        utils.apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_secondary)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    bottomMargin = insets.bottom
                }

                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun setTipContainerByFilter(description: String, ranges: List<Pair<Int, Int>>) {
        setTextZoomDescriptionColorMulti(description, ranges)
        binding.ivPinchZoom.setImageResource(R.drawable.ic_tip)
        binding.ivPinchZoom.layoutParams.height = 52.dpToPx(this)
        binding.ivPinchZoom.layoutParams.width = 52.dpToPx(this)
        binding.ivPinchZoom.requestLayout()
    }

    private fun initTipContainerSpanColor(
        text: String,
        start: Int,
        end: Int
    ) {
        binding.tvZoomDescription.text = SpannableStringUtils(this).toColorSpan(
            R.color.color_action_enabled,
            text, start, end
        )
    }

    private fun setTextZoomDescriptionColorMulti(
        text: String,
        ranges: List<Pair<Int, Int>>
    ) {
        binding.tvZoomDescription.text = SpannableStringUtils(this).toColorSpanMulti(
            R.color.color_action_enabled,
            text, ranges
        )
    }

    private fun interactionWebView() {
        binding.wvStadium.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJavaScriptFunction()
                stopShimmer()
                onClickBottomSheet()
                behavior.isDraggable = true
            }
        }
    }

    private fun startShimmer() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = VISIBLE
        binding.clContainer.visibility = INVISIBLE
        binding.clTipContainer.visibility = INVISIBLE
    }

    private fun stopShimmer() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = INVISIBLE
        binding.clContainer.visibility = VISIBLE
        initTipContainer()
    }

    private fun onClickBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun onClickClose() {
        binding.ivHome.setOnClickListener {
            startToHomeActivity()
        }
    }

    private fun onClickRefresh() {
        binding.btnRefresh.setOnClickListener {
            binding.clTipContainer.visibility = GONE
            refresh()
            setSectionText(
                getString(com.depromeet.presentation.R.string.viewfinder_find_section_description),
                R.color.color_foreground_caption
            )
            viewModel.refreshFilter()
            viewModel.refreshSections()
        }
    }

    private fun onClickBottomSheet() {
        binding.clBottomSheet.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun onClickTipContainer() {
        binding.ivZoomDescriptionClose.setOnClickListener {
            binding.clTipContainer.visibility = GONE
        }
    }

    private fun filterBySection(sectionColor: String) {
        binding.wvStadium.evaluateJavascript("javascript: onActiveBlock('${sectionColor}')", null)
    }

    private fun filterByBlocks(filters: List<String>) {
        val formattedList =
            filters.joinToString(prefix = "[", postfix = "]", transform = { "'$it'" })
        binding.wvStadium.evaluateJavascript("javascript: onActiveBlock($formattedList)", null)
    }

    private fun loadWebView(data: String) {
        binding.wvStadium.loadDataWithBaseURL(
            BASE_URL, data, MIME_TYPE_TEXT_HTML,
            ENCODING_UTF8, null
        )
        binding.wvStadium.webChromeClient = WebChromeClient()
    }

    private fun refresh() {
        binding.btnRefresh.visibility = GONE
        binding.wvStadium.evaluateJavascript("javascript: removeStyle()", null)
    }

    private fun startToStadiumDetailActivity(id: String) {
        Intent(this, StadiumDetailActivity::class.java).apply {
            putExtra(SchemeKey.STADIUM_ID, viewModel.stadiumId)
            putExtra(SchemeKey.BLOCK_CODE, id)
        }.let(::startActivity)
    }

    private fun injectJavaScriptFunction() {
        binding.wvStadium.loadUrl(AndroidBridge.INJECT_STADIUM_BLOCK_NUMBER)
    }

    override fun onDestroy() {
        clearWebViewObject()
        super.onDestroy()
    }

    private fun clearWebViewObject() {
        binding.wvStadium.apply {
            removeJavascriptInterface(AndroidBridge.JAVASCRIPT_OBJ)
            webChromeClient = null
            destroy()
        }
    }

    private fun startToHomeActivity() {
        Intent(
            this,
            HomeActivity::class.java
        ).apply {
            startActivity(this)
            finishAffinity()
        }
    }
}