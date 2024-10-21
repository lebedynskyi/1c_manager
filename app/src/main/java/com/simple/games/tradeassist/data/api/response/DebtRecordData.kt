package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DebtRecordData {
    @SerialName("Period")
    lateinit var time: String

    @SerialName("RecordType")
    lateinit var recordType: String

    @SerialName("Recorder_Type")
    lateinit var documentType: String

    @SerialName("Контрагент_Key")
    lateinit var customerRefKey: String

    @SerialName("Сумма")
    var amount: Double = 0.0

    @SerialName("Договор_Key")
    lateinit var orderDocumentRefKey: String
}