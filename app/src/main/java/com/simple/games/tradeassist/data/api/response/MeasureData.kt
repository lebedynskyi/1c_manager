package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MeasureData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("МеждународноеСокращение")
    var name: String? = null
}