package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity("gods")
class GodsData : java.io.Serializable {
    @PrimaryKey
    @ColumnInfo("refKey")
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @ColumnInfo("parentKey")
    @SerialName("Parent_Key")
    var parentKey: String? = null

    @ColumnInfo("imageFileKey")
    @SerialName("ФайлКартинки_Key")
    var imageKey: String? = null

    @ColumnInfo("measureKey")
    @SerialName("ЕдиницаИзмерения_Key")
    var measureKey: String? = null

    @ColumnInfo("isFolder")
    @SerialName("IsFolder")
    var isFolder: Boolean = false

    @ColumnInfo("code")
    @SerialName("Code")
    var code: String? = null

    @ColumnInfo("description")
    @SerialName("Description")
    var description: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GodsData

        return refKey == other.refKey
    }

    override fun hashCode(): Int {
        return refKey.hashCode()
    }
}