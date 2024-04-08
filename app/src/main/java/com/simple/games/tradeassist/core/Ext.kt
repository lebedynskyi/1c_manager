package com.simple.games.tradeassist.core

fun String?.orDefault(value: String): String {
    return if (isNullOrBlank()) value else this
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return Math.round(this * multiplier) / multiplier
}