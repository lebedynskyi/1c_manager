package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.ui.base.AppViewState
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

data class CreateOrderViewState(
    var customerName: String = "",
    var responsibleName: String? = null,
    var filteredCustomers: List<CustomerData> = emptyList(),
    var addGodsEnabled: Boolean = false,
    var orderTemplates: List<GodOrderTemplate>? = null,
    var responsible: List<ResponsibleData>? = null
) : AppViewState()