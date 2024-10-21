package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

sealed class CreateOrderUIEvent : AppUIEvent() {
    data class OnScreenLoaded(val draftId: Long, val editedGod: GodOrderTemplate?): CreateOrderUIEvent()
    data object OnAddGods: CreateOrderUIEvent()
    data object SaveOrder: CreateOrderUIEvent()
    data object OnDismissCustomerDropDown: CreateOrderUIEvent()
    data class OnCustomerNameChange(val name: String): CreateOrderUIEvent()
    data class OnCommentChanged(val orderComment: String): CreateOrderUIEvent()
    data class OnCustomerSelected(val customer: CustomerData): CreateOrderUIEvent()
    data class OnResponsibleSelected(val responsible: ResponsibleData): CreateOrderUIEvent()
    data class OnGodRemoveClicked(val god: GodOrderTemplate): CreateOrderUIEvent()
    data class OnGodEditClick(val god: GodOrderTemplate): CreateOrderUIEvent()
}