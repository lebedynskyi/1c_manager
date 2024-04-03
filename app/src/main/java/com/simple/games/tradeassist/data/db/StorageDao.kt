package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.StorageRecordData

@Dao
interface StorageDao {
    @Query("SELECT * FROM storage")
    suspend fun getAll(): List<StorageRecordData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<StorageRecordData>)

    @Delete
    suspend fun delete(customers: StorageRecordData)

    @Query("SELECT COUNT(godRefKey) FROM storage")
    suspend fun getCount(): Int
}