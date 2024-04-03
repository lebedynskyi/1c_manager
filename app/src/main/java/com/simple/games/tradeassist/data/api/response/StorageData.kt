package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StorageData {
    @SerialName("Recorder_Type")
    var recorderType: String? = null

    @SerialName("RecordSet")
    var storageSet: List<StorageRecordData> = emptyList()
}

@Entity("storage")
@Serializable
class StorageRecordData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int = 0

    @ColumnInfo("godRefKey")
    @SerialName("Номенклатура_Key")
    lateinit var godKey: String

    @ColumnInfo("period")
    @SerialName("Period")
    var period: String? = null

    @ColumnInfo("amount")
    @SerialName("Количество")
    var amount: Float? = null

    @ColumnInfo("recordType")
    @SerialName("Recorder_Type")
    var recorderType: String? = null
}