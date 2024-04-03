package com.simple.games.tradeassist.domain

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
    var id: Int = 0

    @ColumnInfo("refKey")
    var refKey: String? = null

    @ColumnInfo("customerKey")
    lateinit var customerKey: String
    @ColumnInfo("customerName")
    lateinit var customerName: String
    @ColumnInfo("responsibleKey")
    lateinit var responsibleKey: String
    @ColumnInfo("responsibleName")
    lateinit var responsibleName: String
    @ColumnInfo("gods")
    lateinit var gods: List<GodOrderTemplate>
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