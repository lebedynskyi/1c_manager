package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "measure")
@Serializable
class MeasureData : java.io.Serializable {
    @ColumnInfo("refKey")
    @PrimaryKey
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @ColumnInfo("internationalName")
    @SerialName("МеждународноеСокращение")
    var name: String? = null
}