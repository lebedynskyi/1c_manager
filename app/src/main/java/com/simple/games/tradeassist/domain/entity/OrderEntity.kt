package com.simple.games.tradeassist.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Entity("orders")
class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("localId")
    var id: Long = 0

    @ColumnInfo("refKey")
    var refKey: String? = null

    @ColumnInfo("customerKey")
    var customerKey: String? = null

    @ColumnInfo("customerName")
    var customerName: String? = null

    @ColumnInfo("responsibleKey")
    var responsibleKey: String?= null

    @ColumnInfo("responsibleName")
    var responsibleName: String? = null

    @ColumnInfo("gods")
    var gods: List<GodOrderTemplate>? = null
}

class OrderConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun listToJson(value: List<GodOrderTemplate>?): String {
        return json.encodeToString(ListSerializer(GodOrderTemplate.serializer()), value.orEmpty())
    }

    @TypeConverter
    fun jsonToList(value: String): List<GodOrderTemplate> {
        return json.decodeFromString(ListSerializer(GodOrderTemplate.serializer()), value)
    }
}