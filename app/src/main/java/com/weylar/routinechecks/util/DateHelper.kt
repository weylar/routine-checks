package com.weylar.routinechecks.util

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

object DateHelper {



    fun Long.convertMilliToDateString(): String? {
        val formatter = SimpleDateFormat("EEE, dd MMM, hh:mm aa")
        return try {
            formatter.format(this)
        }catch (e: Exception){
            null
        }
    }

    fun buildDate(year: Int, month:Int, day: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        return calendar.timeInMillis
    }



}