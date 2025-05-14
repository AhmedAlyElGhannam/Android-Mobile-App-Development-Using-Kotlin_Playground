package com.example.task_01

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Product::class), version = 1)
abstract class LocalRoomDB : RoomDatabase() {
    abstract fun getProductDao() : ProductDAO

    companion object {
        @Volatile
        private var INSTANCE : LocalRoomDB? = null

        fun getInstance (context : Context) : LocalRoomDB {
            return INSTANCE ?: synchronized(this) {
                val temp = Room.databaseBuilder(context.applicationContext, LocalRoomDB::class.java, "products").build()
                INSTANCE = temp
                temp
            }
        }
    }
}