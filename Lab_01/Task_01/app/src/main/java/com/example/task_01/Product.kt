package com.example.task_01

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Product(
    var id: Int,
    var title: String,
    var description: String,
    var price: Double,
    var rating: Double,
    var thumbnail: String
) : Serializable
