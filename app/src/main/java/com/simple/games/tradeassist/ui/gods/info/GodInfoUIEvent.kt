package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.domain.GodEntity

sealed class GodInfoUIEvent : AppUIEvent() {
    data object OnAddClick : GodInfoUIEvent()
    data class OnScreenLoaded(
        val god: GodEntity,
        val customerName: String? = null,
        val customerKey: String? = null,
        val price: Float? = null,
        val amount: Float? = null
    ) : GodInfoUIEvent()
    data class OnAmountChanged(val amount: String) : GodInfoUIEvent()
    data class OnPriceChanged(val price: String) : GodInfoUIEvent()
}