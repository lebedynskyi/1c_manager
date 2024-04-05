package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity("price")
@Serializable
class PriceData {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var localId: Int = 0

    @ColumnInfo("date")
    @SerialName("Period")
    var date: String? = null //"2023-06-25T00:00:00"

    @ColumnInfo("priceTypeKey")
    @SerialName("ВидЦен_Key")
    var priceTypeKey: String? = null

    @ColumnInfo("priceTypeName")
    var priceTypeName: String? = null

    @ColumnInfo("godKey")
    @SerialName("Номенклатура_Key")
    var godKey: String? = null

    @ColumnInfo("price")
    @SerialName("Цена")
    var priceValue: Float? = null

    @ColumnInfo("actual")
    @SerialName("Актуальность")
    var actual: Boolean? = null
}

@Serializable
class PriceTypeData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Description")
    val description: String? = null
}