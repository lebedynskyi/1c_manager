package com.simple.games.tradeassist.ui.main

import com.simple.games.tradeassist.ui.base.AppUIEvent

sealed class MainUIEvent : AppUIEvent() {
    data object OnScreenLoaded : MainUIEvent()
    data object OnOrderClick : MainUIEvent()
    data object OnGodsClick : MainUIEvent()
}