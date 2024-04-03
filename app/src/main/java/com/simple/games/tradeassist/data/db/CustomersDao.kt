package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.data.api.response.CustomerData

@Dao
interface CustomersDao {
    @Query("SELECT * FROM customers")
    suspend fun getAll(): List<CustomerData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<CustomerData>)

    @Delete
    suspend fun delete(customers: CustomerData)

    @Query("SELECT COUNT(refKey) FROM customers")
    suspend fun getCount(): Int
}