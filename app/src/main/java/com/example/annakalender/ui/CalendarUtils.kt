package com.example.annakalender.ui

import java.util.Calendar
import java.util.Locale

object CalendarUtils {

    fun getStartOfDay(timeInMillis: Long): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getEndOfDay(timeInMillis: Long): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameMonth(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
    }

    fun addDays(timeInMillis: Long, days: Int): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            add(Calendar.DAY_OF_MONTH, days)
        }.timeInMillis
    }

    fun addWeeks(timeInMillis: Long, weeks: Int): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            add(Calendar.WEEK_OF_YEAR, weeks)
        }.timeInMillis
    }

    fun addMonths(timeInMillis: Long, months: Int): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            add(Calendar.MONTH, months)
        }.timeInMillis
    }

    fun getStartOfWeek(timeInMillis: Long): Long {
        return Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = timeInMillis
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getWeekDays(startOfWeekMillis: Long): List<Long> {
        val list = mutableListOf<Long>()
        val cal = Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = startOfWeekMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        for (i in 0 until 7) {
            list.add(cal.timeInMillis)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    fun getMonthGridDays(selectedDateMillis: Long): List<Long> {
        val targetMonthCal = Calendar.getInstance(Locale.GERMANY).apply {
            this.timeInMillis = selectedDateMillis
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val targetMonth = targetMonthCal.get(Calendar.MONTH)

        // Move back to Monday of that week
        while (targetMonthCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            targetMonthCal.add(Calendar.DAY_OF_MONTH, -1)
        }

        val list = mutableListOf<Long>()
        for (i in 0 until 42) {
            list.add(targetMonthCal.timeInMillis)
            targetMonthCal.add(Calendar.DAY_OF_MONTH, 1)
            // Stop if we have completed at least 35 days (5 full weeks) and moved past target month
            if (i >= 34 && targetMonthCal.get(Calendar.MONTH) != targetMonth && targetMonthCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                break
            }
        }
        return list
    }
}
