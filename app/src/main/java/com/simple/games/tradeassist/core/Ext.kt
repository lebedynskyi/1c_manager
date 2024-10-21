package com.simple.games.tradeassist.core

import java.text.NumberFormat
import java.util.Locale

fun String?.orDefault(value: String): String {
    return if (isNullOrBlank()) value else this
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return Math.round(this * multiplier) / multiplier
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0F
    repeat(decimals) { multiplier *= 10F }
    return Math.round(this * multiplier) / multiplier
}

fun Double?.format(suffix: String?): String {
    if (this == null) return ""

    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return formatter.format(this) + suffix.orEmpty()
}