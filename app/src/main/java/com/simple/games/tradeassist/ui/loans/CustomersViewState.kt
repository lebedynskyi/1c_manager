package com.simple.games.tradeassist.ui.loans

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppViewState

data class CustomersViewState(
    var customers: List<CustomerData>
) : AppViewState()