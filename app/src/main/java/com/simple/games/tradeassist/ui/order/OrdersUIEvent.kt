package com.simple.games.tradeassist.ui.order

import com.simple.games.tradeassist.domain.OrderEntity
import com.simple.games.tradeassist.ui.base.AppUIEvent

sealed class OrdersUIEvent : AppUIEvent() {
    object OnScreenLoaded : OrdersUIEvent()
    object CreateOrder : OrdersUIEvent()
    object OnDraftsClicked : OrdersUIEvent()
    object OnHistoryClicked : OrdersUIEvent()

    class OnPublishClick(val order: OrderEntity) : OrdersUIEvent()
    class OnDeleteClick(val order: OrderEntity) : OrdersUIEvent()
    class OnEditClick(val order: OrderEntity) : OrdersUIEvent()
}