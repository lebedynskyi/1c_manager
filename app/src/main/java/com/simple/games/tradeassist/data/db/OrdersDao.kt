package com.simple.games.tradeassist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.games.tradeassist.domain.OrderEntity

@Dao
interface OrdersDao {
    @Query("SELECT * FROM orders")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE refKey is NULL")
    suspend fun getDrafts(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE refKey is NULL AND localId = :localId")
    suspend fun getDraft(localId: Long): OrderEntity

    @Query("SELECT * FROM orders WHERE refKey is not NULL")
    suspend fun getPublished(): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity) : Long

    @Delete
    suspend fun delete(customers: OrderEntity)

    @Query("SELECT COUNT(localId) FROM orders")
    suspend fun getCount(): Int
}