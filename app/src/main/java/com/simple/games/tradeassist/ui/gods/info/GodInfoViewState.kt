package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.ui.base.AppViewState

data class GodInfoViewState(
    override var requestInProgress: Boolean = false,
    var godsEntity: GodEntity? = null,
    var orderHistory: List<Pair<String, GodOrderData>>? = null,
    var historyName: String? = null,
    var addBtnEnabled: Boolean = false,
    var amountInput: String? = null,
    var amountError: Boolean = false,
    var priceMarga: Double = 0.0,
    var priceInput: String? = null,
    var priceError: Boolean = false
) : AppViewState()