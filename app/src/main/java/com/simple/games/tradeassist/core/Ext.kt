package com.simple.games.tradeassist.core

fun String?.orDefault(value: String): String {
    return if (isNullOrBlank()) value else this
}