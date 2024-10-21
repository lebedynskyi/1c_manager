package com.simple.games.tradeassist.ui.loans

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppUIEvent

sealed class CustomersUIEvent : AppUIEvent() {
    object OnScreenLoaded : CustomersUIEvent()
    data class OnCustomerClicked(val customer: CustomerData) : CustomersUIEvent()
}