package com.simple.games.tradeassist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.CustomersConverter
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageRecordData
import com.simple.games.tradeassist.domain.entity.OrderConverter
import com.simple.games.tradeassist.domain.entity.OrderEntity


@Database(
    version = 2,
    entities = [
        CustomerData::class,
        MeasureData::class,
        GodsData::class,
        StorageRecordData::class,
        ResponsibleData::class,
        PriceData::class,
        OrderEntity::class
    ],
)

@TypeConverters(CustomersConverter::class, OrderConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun customersDao(): CustomersDao
    abstract fun measureDao(): MeasureDao
    abstract fun godsDao(): GodsDao
    abstract fun storageDao(): StorageDao
    abstract fun responsibleDao(): ResponsibleDao
    abstract fun ordersDao(): OrdersDao
    abstract fun priceDao(): PriceDao
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'orders' ADD COLUMN 'orderComment' TEXT")
        database.execSQL("ALTER TABLE 'orders' ADD COLUMN 'publishDate' INTEGER")
    }
}