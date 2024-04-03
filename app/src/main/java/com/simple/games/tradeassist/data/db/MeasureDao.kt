package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.MeasureData

@Dao
interface MeasureDao {
    @Query("SELECT * FROM measure")
    suspend fun getAll(): List<MeasureData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<MeasureData>)

    @Delete
    suspend fun delete(customers: MeasureData)

    @Query("SELECT COUNT(refKey) FROM measure")
    suspend fun getCount(): Int
}