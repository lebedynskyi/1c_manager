package com.simple.games.tradeassist.data.api.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity("customers")
@Serializable
class CustomerData : java.io.Serializable {
    @PrimaryKey
    @ColumnInfo(name = "refKey")
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @ColumnInfo(name = "parentKey")
    @SerialName("Parent_Key")
    lateinit var parentKey: String

    @ColumnInfo(name = "isFolder")
    @SerialName("IsFolder")
    var isFolder: Boolean = false

    @ColumnInfo(name = "description")
    @SerialName("Description")
    var description: String? = null

    // TODO not used info ?
    @ColumnInfo(name = "contactInfo")
    @SerialName("КонтактнаяИнформация")
    var contact: List<CustomerContactData>? = null

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

@Serializable
class CustomerContactData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Представление")
    var description: String? = null
}

class CustomersConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun listToJson(value: List<CustomerContactData>?): String {
        return json.encodeToString(ListSerializer(CustomerContactData.serializer()), value.orEmpty())
    }

    @TypeConverter
    fun jsonToList(value: String): List<CustomerContactData> {
        return json.decodeFromString(ListSerializer(CustomerContactData.serializer()), value)
    }
}