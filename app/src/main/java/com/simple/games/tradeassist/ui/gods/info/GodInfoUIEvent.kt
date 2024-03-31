package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData

sealed class GodInfoUIEvent : AppUIEvent() {
    data object OnAddClick : GodInfoUIEvent()
    data class OnScreenLoaded(
        val customer: CustomerData,
        val god: GodsData,
        val price: Float? = null,
        val amount: Float? = null
    ) : GodInfoUIEvent()
    data class OnAmountChanged(val amount: String) : GodInfoUIEvent()
    data class OnPriceChanged(val price: String) : GodInfoUIEvent()
}