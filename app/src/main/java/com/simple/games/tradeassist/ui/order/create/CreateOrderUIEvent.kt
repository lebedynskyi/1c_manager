package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

sealed class CreateOrderUIEvent : AppUIEvent() {
    data class  OnScreenLoaded(val draftId: Long): CreateOrderUIEvent()
    object OnAddGods: CreateOrderUIEvent()
    object SaveOrder: CreateOrderUIEvent()
    object OnDismissCustomerDropDown: CreateOrderUIEvent()
    data class OnGodsAdded(val gods: List<GodOrderTemplate>): CreateOrderUIEvent()
    data class OnGodEdited(val god: GodOrderTemplate): CreateOrderUIEvent()
    data class OnCustomerNameChange(val name: String): CreateOrderUIEvent()
    data class OnCustomerSelected(val customer: CustomerData): CreateOrderUIEvent()
    data class OnResponsibleSelected(val responsible: ResponsibleData): CreateOrderUIEvent()
    data class OnGodRemoveClicked(val god: GodOrderTemplate): CreateOrderUIEvent()
    data class OnGodEditClick(val god: GodOrderTemplate): CreateOrderUIEvent()
}