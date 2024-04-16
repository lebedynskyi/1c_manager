package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.ResponsibleData

@Dao
interface ResponsibleDao {
    @Query("SELECT * FROM responsible")
    suspend fun getAll(): List<ResponsibleData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<ResponsibleData>)

    @Query("DELETE FROM responsible")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(customers: ResponsibleData)

    @Query("SELECT COUNT(refKey) FROM responsible")
    suspend fun getCount(): Int
}