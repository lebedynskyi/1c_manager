package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class  DataResponse<T> {
    @SerialName("value")
    val data: List<T> = emptyList()
}