package com.dpm.designsystem

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.depromeet.designsystem.R

/***
 * [SpotSpinner]
 * [selectedResource] : 스피너 배경 설정 자세한 사항은 (item_custom_month_spinner_view.xml 참고)
 * [dropDownResource] : 스피너 드롭다운 배경 설정으로 (item_custom_month_spinner_dropdown.xml 참고)
 * [isEmphasize] 가
 * true : 드롭다운에 강조하고 싶은 부분 selectedResouce layout 적용
 * false : 스피너와 드롭다운만 각각 layout 적용
 * [isEmphasize] 이면 [emphasizeColor] (textview color) 도 설정 (dropdown은 따로 layout적용 불가)
 */
class SpotSpinner<T>(
    context: Context,
    private val selectedResource: Int,
    private val dropDownResource: Int,
    data: List<T>,
    private val isEmphasize: Boolean = false,
    private val emphasizeColor: Int = R.color.black,
) : ArrayAdapter<T>(context, selectedResource, data) {

    private var selectedItemPosition = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(selectedResource, parent, false)
        val item = getItem(position)
        (view as TextView).text = item.toString()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(dropDownResource, parent, false)
        val textView = view as TextView
        val item = getItem(position)

        textView.text = item.toString()
        if (isEmphasize && position == selectedItemPosition) {
            textView.setTextColor(ContextCompat.getColor(context, emphasizeColor))
            textView.setTypeface(null, Typeface.BOLD)
        }
        return view
    }


    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
}