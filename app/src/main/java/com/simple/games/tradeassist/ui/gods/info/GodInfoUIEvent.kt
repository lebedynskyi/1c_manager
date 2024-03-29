package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class GodInfoUIEvent : AppUIEvent() {
    data object OnAddClick : GodInfoUIEvent()
    data class OnScreenLoaded(val customerKey: String, val godKey: String) : GodInfoUIEvent()
    data class OnAmountChanged(val amount: String) : GodInfoUIEvent()
    data class OnPriceChanged(val price: String) : GodInfoUIEvent()
}