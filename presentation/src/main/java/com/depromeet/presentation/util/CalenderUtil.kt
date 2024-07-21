package com.depromeet.presentation.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    private const val DATE_FORMAT = "yy.MM.dd"

    fun formatCalendarDate(calendar: Calendar): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.time)
    }

    fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    fun getCalendar(year: Int, month: Int, day: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar
    }

    fun getYear(calendar: Calendar): Int {
        return calendar.get(Calendar.YEAR)
    }

    fun getMonth(calendar: Calendar): Int {
        return calendar.get(Calendar.MONTH)
    }

    fun getDay(calendar: Calendar): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}
