package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.ui.base.AppViewState

data class GodInfoViewState(
    override var requestInProgress: Boolean = false,
    var godsData: GodsData? = null,
    var orderHistory: List<Pair<String, GodOrderData>>? = null
) : AppViewState()