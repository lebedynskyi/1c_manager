package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppViewState

data class CreateOrderViewState(
    var customerName: String = "",
    var filteredCustomers: List<CustomerData> = emptyList(),
    var selectedCustomer: CustomerData? = null,
    var addGodsEnabled: Boolean = false,
) : AppViewState()