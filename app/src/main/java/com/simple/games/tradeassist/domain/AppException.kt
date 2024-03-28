package com.simple.games.tradeassist.domain

sealed class AppException(msg: String) : Exception(msg) {
    data object  Unknown: AppException("Unknown")
    data object NoInternet : AppException("No Internet Connection")
}