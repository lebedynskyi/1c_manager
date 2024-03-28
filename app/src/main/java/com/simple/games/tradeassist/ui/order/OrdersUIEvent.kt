package com.simple.games.tradeassist.ui.order

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class OrdersUIEvent: AppUIEvent() {
    object CreateOrder: OrdersUIEvent()
    object OnDraftsClicked: OrdersUIEvent()
    object OnHistoryClicked: OrdersUIEvent()
}