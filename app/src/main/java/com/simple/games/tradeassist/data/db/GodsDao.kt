package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.GodsData

@Dao
interface GodsDao {
    @Query("SELECT * FROM gods")
    suspend fun getAll(): List<GodsData>

    @Query("SELECT * FROM gods WHERE refKey = :argKey")
    suspend fun get(argKey: String): GodsData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<GodsData>)

    @Delete
    suspend fun delete(customers: GodsData)

    @Query("SELECT COUNT(refKey) FROM gods")
    suspend fun getCount(): Int
}