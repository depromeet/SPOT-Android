package com.depromeet.designsystem

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SpotDropDownSpinner<T>(
    private val data: List<T>,
) : BaseAdapter() {

    private var selectedItemPosition = 0

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): T = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spot_spinner_view, parent, false)
        val textView = view.findViewById<TextView>(R.id.spot_spinner_view)
        val item = getItem(position)

        textView.text = item.toString()
        if (position == selectedItemPosition) {
            textView.setTextColor(parent.context.getColor(R.color.color_foreground_heading))
            textView.setTypeface(null, Typeface.BOLD)
        } else {
            textView.setTextColor(parent.context.getColor(R.color.color_foreground_body_sebtext))
            textView.setTypeface(null, Typeface.NORMAL)
        }

        textView.text = item.toString()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spot_spinner_dropdown, parent, false)
        val textView = view.findViewById<TextView>(R.id.spot_spinner_dropdown)
        val item = getItem(position)

        textView.text = item.toString()
        if (position == selectedItemPosition) {
            textView.setTextColor(parent.context.getColor(R.color.color_foreground_heading))
            textView.setTypeface(null, Typeface.BOLD)
        } else {
            textView.setTextColor(parent.context.getColor(R.color.color_foreground_body_sebtext))
            textView.setTypeface(null, Typeface.NORMAL)
        }

        textView.text = item.toString()

        return view
    }

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
}



