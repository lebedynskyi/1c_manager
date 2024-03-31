package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class OrderHistoryData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Date")
    lateinit var date: String

    @SerialName("Запасы")
    val gods: List<GodOrderData>? = null
}

@Serializable
class GodOrderData {
    @SerialName("Номенклатура_Key")
    lateinit var refKey: String

    @SerialName("ЕдиницаИзмерения")
    val measureKey: String? = null

    @SerialName("Количество")
    var amount: Int? = null

    @SerialName("Цена")
    var price: Float? = null

    @SerialName("Сумма")
    var sum: Float? = null

    @Transient
    var measure: MeasureData? = null
}