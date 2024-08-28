package com.dpm.designsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.depromeet.designsystem.R
import com.dpm.designsystem.extension.dpToPx

class SpotDropDownSpinner<T>(
    private var data: List<T>,
    initialSelectedPosition: Int = 0,
) : BaseAdapter() {

    private var selectedItemPosition = initialSelectedPosition

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): T = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spot_spinner_view, parent, false)
        val textView = view.findViewById<TextView>(R.id.spot_spinner_view)
        val item = getItem(position)

        textView.setTextColor(parent.context.getColor(R.color.color_foreground_heading))
        textView.setTextAppearance(R.style.TextAppearance_Spot_Subtitle01)

        textView.text = data[selectedItemPosition].toString()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spot_spinner_dropdown, parent, false)
        val textView = view.findViewById<TextView>(R.id.spot_spinner_dropdown)
        val dividerView = view.findViewById<View>(R.id.divider_view)
        val item = getItem(position)

        textView.text = item.toString()
        updateTextAppearance(textView, position == selectedItemPosition)

        val topPadding = if (position == 0) 10.dpToPx(view.context) else 0
        val bottomPadding = if (position == data.size - 1) 10.dpToPx(view.context) else 0
        textView.setPadding(textView.paddingStart, topPadding, textView.paddingEnd, bottomPadding)

        if (position == data.size - 1) {
            dividerView.visibility = View.GONE
        } else {
            dividerView.visibility = View.VISIBLE
        }

        return view
    }

    private fun updateTextAppearance(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setTextColor(textView.context.getColor(R.color.color_foreground_heading))
            textView.setTextAppearance(R.style.TextAppearance_Spot_Subtitle02)
        } else {
            textView.setTextColor(textView.context.getColor(R.color.color_gray_700))
            textView.setTextAppearance(R.style.TextAppearance_Spot_Body01)
        }
    }

    fun updateData(newData: List<T>, selectedPosition: Int) {
        data = newData
        selectedItemPosition = selectedPosition
        notifyDataSetChanged()
    }

    fun setSelectedItemPosition(position: Int) {
        if (position != selectedItemPosition) {
            selectedItemPosition = position
            notifyDataSetChanged()
        }
    }
}




