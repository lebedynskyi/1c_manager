package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StorageData {
    @SerialName("Recorder_Type")
    var recorderType: String? = null

    @SerialName("Номенклатура_Key")
    var refKey: String? = null

    @SerialName("Period")
    var period: String? = null

    @SerialName("Количество")
    var amount: Float? = null

//    @SerialName("RecordSet")
//    var storageSet: List<StorageRecordSet> = emptyList()
}

@Serializable
class StorageRecordSet {
    @SerialName("Номенклатура_Key")
    var refKey: String? = null

    @SerialName("Period")
    var period: String? = null

    @SerialName("Количество")
    var amount: Float? = null
}