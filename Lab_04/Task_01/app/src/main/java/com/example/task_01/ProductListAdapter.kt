package com.example.task_01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProductListAdapter(val listener : (Product) -> Unit) : ListAdapter<Product, ProductListAdapter.ProductViewHolder> (ProductDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_row, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currProduct = getItem(position)
        holder.titleTxt.text = currProduct.title
        holder.productImg.setImageResource(currProduct.thumbnail)
        holder.layout.setOnClickListener {
            listener.invoke(currProduct)
        }
    }

    public class ProductViewHolder(private val item : View) : RecyclerView.ViewHolder(item) {
        var titleTxt : TextView = item.findViewById(R.id.txtViewTitle)
        var productImg : ImageView = item.findViewById(R.id.imgView)
        var layout : ConstraintLayout = item.findViewById(R.id.productLayout)
    }
}