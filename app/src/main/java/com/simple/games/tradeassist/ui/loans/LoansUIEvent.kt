package com.simple.games.tradeassist.ui.loans

import com.simple.games.tradeassist.ui.base.AppUIEvent

sealed class LoansUIEvent : AppUIEvent() {
    object OnScreenLoaded : LoansUIEvent()
}