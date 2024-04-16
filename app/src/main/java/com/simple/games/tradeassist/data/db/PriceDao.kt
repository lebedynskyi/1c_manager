package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.PriceData

@Dao
interface PriceDao {
    @Query("SELECT * FROM price")
    suspend fun getAll(): List<PriceData>

    @Query("SELECT * FROM price WHERE godKey = :godKey")
    suspend fun getForGod(godKey: String): PriceData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<PriceData>)

    @Query("DELETE FROM price")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(customers: PriceData)

    @Query("SELECT COUNT(localId) FROM price")
    suspend fun getCount(): Int
}