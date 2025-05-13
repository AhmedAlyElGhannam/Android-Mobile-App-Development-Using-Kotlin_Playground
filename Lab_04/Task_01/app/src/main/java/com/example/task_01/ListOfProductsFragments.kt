package com.example.task_01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListOfProductsFragments : Fragment(){
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: ProductListAdapter
    lateinit var communicator : Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.list_frag, container, false)

        val productArr = arrayOf(
            Product("1", "Product One Title", "Product One Description", "9001", R.drawable.one),
            Product("2", "Product Two Title", "Product Two Description", "1006", R.drawable.two),
            Product("3", "Product Three Title", "Product Three Description", "9001", R.drawable.three),
            Product("4", "Product Four Title", "Product Four Description", "1006", R.drawable.four),
            Product("5", "Product Five Title", "Product Five Description", "9001", R.drawable.five)
            )

        recyclerView = view.findViewById(R.id.recyclerView)

        communicator = activity as Communicator
        val adapterListener : (Product) -> Unit = {
            // move product to details fragment through communicator
            communicator.showProductDetails(it)
        }
        recyclerAdapter = ProductListAdapter(adapterListener)
        recyclerAdapter.submitList(productArr.toList())


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.hasFixedSize()
        val layoutManager : LinearLayoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
    }
}