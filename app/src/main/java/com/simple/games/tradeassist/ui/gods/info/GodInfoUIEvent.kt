package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class GodInfoUIEvent: AppUIEvent()
{
    data class OnScreenLoaded(val customerKey: String, val godKey: String): GodInfoUIEvent()
}