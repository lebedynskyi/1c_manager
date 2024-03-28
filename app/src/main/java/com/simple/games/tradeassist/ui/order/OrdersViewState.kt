package com.simple.games.tradeassist.ui.order

import com.simple.games.tradeassist.ui.base.AppViewState

data class OrdersViewState(
    var selectedTab :Int = 0,
    var isDrafts :Boolean = true,
    var orders: List<Any> = emptyList(),
    override var contentInProgress: Boolean = false
) : AppViewState()