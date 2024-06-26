package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class OrderHistoryData :java.io.Serializable{
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Date")
    lateinit var date: String

    @SerialName("Запасы")
    val gods: List<GodOrderData>? = null
}

@Serializable
class GodOrderData: java.io.Serializable {
    @SerialName("Номенклатура_Key")
    lateinit var refKey: String

    @SerialName("ЕдиницаИзмерения")
    val measureKey: String? = null

    @SerialName("Количество")
    var amount: Float? = null

    @SerialName("Цена")
    var price: Float? = null

    @SerialName("Сумма")
    var sum: Float? = null

    @Transient
    var measure: MeasureData? = null
}