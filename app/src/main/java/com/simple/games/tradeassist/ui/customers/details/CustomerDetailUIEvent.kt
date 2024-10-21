package com.simple.games.tradeassist.ui.customers.details

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppUIEvent

sealed class CustomerDetailUIEvent : AppUIEvent() {
    data class ScreenLoaded(val customerData: CustomerData) : CustomerDetailUIEvent()
}