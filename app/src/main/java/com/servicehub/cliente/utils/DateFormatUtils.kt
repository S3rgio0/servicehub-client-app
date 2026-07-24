package com.servicehub.cliente.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Converts timestamps into a readable date-time string. */
object DateFormatUtils {
    private val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun format(timestamp: Long): String = formatter.format(Date(timestamp))
}
