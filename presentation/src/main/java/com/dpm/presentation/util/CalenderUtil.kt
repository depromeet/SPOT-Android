package com.dpm.presentation.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    private const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm"

    private val ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT)
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    private val DOT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.M.d", Locale.KOREAN)

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

    private fun parseDate(date: String): LocalDateTime {
        return listOf(ISO_DATE_FORMATTER, DATE_FORMATTER).asSequence()
            .mapNotNull { formatter ->
                runCatching {
                    LocalDateTime.parse(date, formatter)
                }.getOrNull()
            }
            .firstOrNull()
            ?: throw IllegalArgumentException("지원하지 않는 날짜 형식입니다: $date")
    }

    fun getYearFromDateFormat(date: String): Int {
        return parseDate(date).year
    }

    fun getMonthFromDateFormat(date: String): Int {
        return parseDate(date).monthValue
    }

    fun getDayOfMonthFromDateFormat(date: String): Int {
        return parseDate(date).dayOfMonth
    }

    fun getDayOfWeekFromDateFormat(date: String): String {
        return parseDate(date).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
    }

    fun getFormattedDate(date: String): String {
        return parseDate(date).format(DISPLAY_FORMATTER)
    }

    fun getFormattedDotDate(date: String): String {
        return parseDate(date).format(DOT_DATE_FORMATTER)
    }

    fun getCurrentYear(): Int {
        val today = LocalDate.now()
        return today.year
    }

}
