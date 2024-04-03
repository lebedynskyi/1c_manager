package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity("responsible")
class ResponsibleData {
    @PrimaryKey
    @SerialName("Ref_Key")
    @ColumnInfo("refKey")
    lateinit var refKey: String

    @ColumnInfo("name")
    @SerialName("Description")
    var name: String? = null
}