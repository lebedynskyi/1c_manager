package com.simple.games.tradeassist.ui.order.create

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData

sealed class CreateOrderUIEvent : AppUIEvent() {
    data object OnScreenLoaded: CreateOrderUIEvent()
    object OnAddGods: CreateOrderUIEvent()
    object OnDismissCustomerDropDown: CreateOrderUIEvent()
    data class OnGodSelected(val resultGodKey: String): CreateOrderUIEvent()
    data class OnCustomerNameChange(val name: String): CreateOrderUIEvent()
    data class OnCustomerSelected(val customer: CustomerData): CreateOrderUIEvent()

}