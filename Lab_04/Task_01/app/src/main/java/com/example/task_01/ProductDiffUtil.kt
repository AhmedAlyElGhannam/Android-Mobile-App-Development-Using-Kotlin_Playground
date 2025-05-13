package com.example.task_01

import androidx.recyclerview.widget.DiffUtil

class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return (oldItem == newItem)
    }

}