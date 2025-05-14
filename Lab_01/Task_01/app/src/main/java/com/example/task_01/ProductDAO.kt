package com.example.task_01

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDAO {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts() : List<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product : Product)
}