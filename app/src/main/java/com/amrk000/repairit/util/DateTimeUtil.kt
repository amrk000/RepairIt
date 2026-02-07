package com.amrk000.repairit.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtil {

    private const val DISPLAY_PATTERN = "MM/dd/yyyy hh:mm a"

    private val formatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern(DISPLAY_PATTERN, Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    fun formatIsoDate(isoDateString: String?): String {
        if (isoDateString.isNullOrEmpty()) return ""

        return try {
            formatter.format(Instant.parse(isoDateString))
        } catch (e: Exception) {
            e.printStackTrace()
            isoDateString
        }
    }
}