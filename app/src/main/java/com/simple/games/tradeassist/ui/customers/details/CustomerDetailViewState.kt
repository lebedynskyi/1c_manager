package com.simple.games.tradeassist.ui.customers.details

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.domain.entity.CustomerDebtEntity
import com.simple.games.tradeassist.ui.base.AppViewState

data class CustomerDetailViewState(
    var customerData: CustomerData? = null,
    var debtEntity: CustomerDebtEntity?= null
    // list of transactions
): AppViewState()