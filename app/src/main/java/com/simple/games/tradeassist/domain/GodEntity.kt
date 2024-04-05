package com.simple.games.tradeassist.domain

import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.PriceData
import kotlinx.serialization.Serializable

@Serializable
data class GodEntity(
    val data: GodsData,
    val measureData: MeasureData? = null,
    val price: List<PriceData> = emptyList(),
    val availableAmount: Float? = 0F
) : java.io.Serializable