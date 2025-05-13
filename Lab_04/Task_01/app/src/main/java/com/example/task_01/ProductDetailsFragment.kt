package com.example.task_01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProductDetailsFragment : Fragment() {

    lateinit var txtViewDesc : TextView
    lateinit var txtViewPrice : TextView
    lateinit var txtViewTitle : TextView
    lateinit var imgView : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.product_details, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtViewTitle = view.findViewById(R.id.txtViewTitle)
        txtViewDesc = view.findViewById(R.id.txtViewDesc)
        txtViewPrice = view.findViewById(R.id.txtViewPrice)
        imgView = view.findViewById(R.id.imgView)
    }

    public fun populateProductDetailsItems(product: Product?) {

        txtViewTitle.text = product?.title
        txtViewDesc.text = product?.description
        txtViewPrice.text = product?.price
        product?.thumbnail?.let { imgView.setImageResource(it) }
    }
}