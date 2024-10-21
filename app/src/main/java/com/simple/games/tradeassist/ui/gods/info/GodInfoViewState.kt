package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.domain.entity.GodEntity
import com.simple.games.tradeassist.ui.base.AppViewState

data class GodInfoViewState(
    override var requestInProgress: Boolean = false,
    var godsEntity: GodEntity? = null,
    var orderHistory: List<Pair<String, GodOrderData>>? = null,
    var historyName: String? = null,
    var addBtnEnabled: Boolean = false,
    var amount: Float? = null,
    var price: Float? = null,
    var marga: Float? = null,
    var debtAmount: Double? = null
) : AppViewState()