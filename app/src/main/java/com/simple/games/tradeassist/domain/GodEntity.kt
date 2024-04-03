package com.simple.games.tradeassist.domain

import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.MeasureData
import kotlinx.serialization.Serializable

@Serializable
data class GodEntity(
    val data: GodsData,
    val measureData: MeasureData?,
    val price: Float,
    val availableAmount: Float
): java.io.Serializable