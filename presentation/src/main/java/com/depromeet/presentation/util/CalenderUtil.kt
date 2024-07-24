package com.depromeet.presentation.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    private const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
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

    fun getMonthFromDateFormat(date: String): Int {
        val formatter = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)
        val parsedDate = LocalDateTime.parse(date, formatter)
        return parsedDate.monthValue
    }

    fun getDayOfMonthFromDateFormat(date: String): Int {
        val formatter = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)
        val parsedDate = LocalDateTime.parse(date, formatter)
        return parsedDate.dayOfMonth
    }

    fun getDayOfWeekFromDateFormat(date: String): String {
        val formatter = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)
        val parsedDate = LocalDateTime.parse(date, formatter)
        return parsedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
    }

    fun getFormattedDate(date: String): String {
        val formatter = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)
        val parsedDate = LocalDateTime.parse(date, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)
        return parsedDate.format(outputFormatter)
    }

    fun getCurrentYear(): Int {
        val today = LocalDate.now()
        return today.year
    }

}
