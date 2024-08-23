package com.dpm.presentation.seatrecord

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dpm.presentation.seatrecord.viewholder.RecordSelectReviewViewHolder
import timber.log.Timber


class HeaderItemDecoration(
    parent: RecyclerView,
    private val shouldFadeOutHeader: Boolean = false,
    private val isHeader: (itemPosition: Int) -> Boolean,
) : RecyclerView.ItemDecoration() {

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    init {
        parent.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                currentHeader = null
            }
        })

        parent.doOnEachNextLayout {
            currentHeader = null
        }
        parent.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                motionEvent: MotionEvent,
            ): Boolean {
                //return false
                //TODO : 우선 false처리로 부모에서 처리 로 진행 추후 수정 예정
                /**
                 *  1. 모두 false로 할 경우
                 *  -> 보여줄수 있지만 sticky header에 대한 클릭 이벤트를 부모가 가져감
                 *  2.아래 코드 문제점
                 *  currentHeader는 스크롤 전에는 모두 정상작동을함
                 *  하지만 stickyheader 이후 header가 해제되었을 때 프로필 클릭이 안됨
                 *  만약 해결했다면 currentHeader에 대한 클릭 리스너를 처리해줘야함 -> 자식 클릭?
                 */
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val y = motionEvent.y.toInt()

                    val headerView = currentHeader?.second?.itemView ?: return false
                    val headerBottom = headerView.bottom + recyclerView.paddingTop
                    val headerTop = headerView.top + recyclerView.paddingTop

                    Timber.d("test -> 위치 ${headerView.bottom} / ${headerView.top} / ${recyclerView.paddingTop}")
                    if (y in headerTop..headerBottom) {
                        Timber.d("test -> true니? ${y in (headerTop..headerBottom)}")

                        val headerPosition = currentHeader?.first ?: return false
                        val headerViewHolder = currentHeader?.second ?: return false

                        if (headerViewHolder is RecordSelectReviewViewHolder) {
                            val seatViewBounds = Rect()
                            val intuitiveViewBounds = Rect()

                            headerViewHolder.binding.tvSeatView.getHitRect(seatViewBounds)
                            headerViewHolder.binding.tvIntuitiveReview.getHitRect(intuitiveViewBounds)

                            if (seatViewBounds.contains(motionEvent.x.toInt(), motionEvent.y.toInt())) {
                                headerViewHolder.binding.tvSeatView.performClick()
                            }

                            if (intuitiveViewBounds.contains(motionEvent.x.toInt(), motionEvent.y.toInt())) {
                                headerViewHolder.binding.tvIntuitiveReview.performClick()
                            }

                            return true
                        }
                    }
                }
                Timber.d("test -> false니? ㅇㅇ")
                return false

            }
        })
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.findChildViewUnder(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat()
        ) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        val contactPoint = headerView.bottom + parent.paddingTop
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        if (isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, headerView, childInContact, parent.paddingTop)
            return
        }

        drawHeader(c, headerView, parent.paddingTop)
    }

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) {
            return null
        }
        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null
        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null
        if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
            return currentHeader?.second?.itemView
        }

        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        if (headerHolder != null) {
            parent.adapter?.onBindViewHolder(headerHolder, headerPosition)
            fixLayoutSize(parent, headerHolder.itemView)
            currentHeader = headerPosition to headerHolder
        }
        return headerHolder?.itemView
    }

    private fun drawHeader(c: Canvas, header: View, paddingTop: Int) {
        c.save()
        c.translate(0f, paddingTop.toFloat())
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View, paddingTop: Int) {
        c.save()
        if (!shouldFadeOutHeader) {
            c.clipRect(0, paddingTop, c.width, paddingTop + currentHeader.height)
        } else {
            c.saveLayerAlpha(
                RectF(0f, 0f, c.width.toFloat(), c.height.toFloat()),
                (((nextHeader.top - paddingTop) / nextHeader.height.toFloat()) * 255).toInt()
            )

        }
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat() /*+ paddingTop*/)

        currentHeader.draw(c)
        if (shouldFadeOutHeader) {
            c.restore()
        }
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            if (mBounds.bottom > contactPoint) {
                if (mBounds.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
        action(
            view
        )
    }
}