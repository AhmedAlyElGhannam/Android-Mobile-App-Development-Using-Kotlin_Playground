package com.example.task_01

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    var id: Int,
    var title: String,
    var description: String,
    var price: Double,
    var rating: Double,
    var thumbnail: String
) : Serializable
