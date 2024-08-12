package com.depromeet.designsystem

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

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
        textView.setTypeface(null, Typeface.BOLD)

        textView.text = data[selectedItemPosition].toString()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spot_spinner_dropdown, parent, false)
        val textView = view.findViewById<TextView>(R.id.spot_spinner_dropdown)
        val item = getItem(position)

        textView.text = item.toString()
        updateTextAppearance(textView, position == selectedItemPosition)

        return view
    }

    private fun updateTextAppearance(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setTextColor(textView.context.getColor(R.color.color_foreground_heading))
            textView.setTypeface(null, Typeface.BOLD)
        } else {
            textView.setTextColor(textView.context.getColor(R.color.color_foreground_body_sebtext))
            textView.setTypeface(null, Typeface.NORMAL)
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




