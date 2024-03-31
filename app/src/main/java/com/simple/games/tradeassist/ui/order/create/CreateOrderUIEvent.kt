package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

sealed class CreateOrderUIEvent : AppUIEvent() {
    data object OnScreenLoaded: CreateOrderUIEvent()
    object OnAddGods: CreateOrderUIEvent()
    object OnDismissCustomerDropDown: CreateOrderUIEvent()
    data class OnGodsAdded(val gods: List<GodOrderTemplate>): CreateOrderUIEvent()
    data class OnGodAdded(val god: GodOrderTemplate): CreateOrderUIEvent()
    data class OnCustomerNameChange(val name: String): CreateOrderUIEvent()
    data class OnCustomerSelected(val customer: CustomerData): CreateOrderUIEvent()
    data class OnGodRemoveClicked(val god: GodOrderTemplate): CreateOrderUIEvent()
    data class OnGodEditClick(val god: GodOrderTemplate): CreateOrderUIEvent()
}