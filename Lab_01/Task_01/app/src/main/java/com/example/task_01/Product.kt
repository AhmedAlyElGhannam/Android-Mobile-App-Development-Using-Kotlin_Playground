package com.example.task_01

import java.io.Serializable

data class Product(
    var id: String,
    var title: String,
    var description: String,
    var price: String,
    var thumbnail: Int
    ) : Serializable