package com.example.task_01

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListOfProductsFragments : Fragment(){
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: ProductListAdapter
    lateinit var communicator : Communicator
    private var listOfProducts : List<Product>? = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.list_frag, container, false)

        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }

        val isConnected = networkCapabilities?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false

        if (isConnected) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val client : ProductClient? = ProductClient.instance
                    val dao : ProductDAO = LocalRoomDB.getInstance(requireContext()).getProductDao()
                    listOfProducts = client?.makeNetworkCall()
                    if (listOfProducts.isNullOrEmpty()) {
                        Log.i("TAG", "onCreateView: $listOfProducts")
                    }
                    else {
                        // store in local db
                        for (i in 0 until listOfProducts!!.size) {
                            dao.insertProduct(listOfProducts!![i])
                        }
                        withContext(Dispatchers.Main) {
                            recyclerAdapter.submitList(listOfProducts)
                        }
                    }
                }
                catch (_: Throwable) {
                    Log.i("TAG", "onCreateView: error while fetching via retrofit")
                }
            }
        }
        else {
            lifecycleScope.launch {
                val dao : ProductDAO = LocalRoomDB.getInstance(requireContext()).getProductDao()
                listOfProducts = dao.getAllProducts()
                if (listOfProducts.isNullOrEmpty()) {
                    Log.i("TAG", "onCreateView: $listOfProducts")
                }
                else {
                    // populate list with locally-stored elements
                    withContext(Dispatchers.Main) {
                        recyclerAdapter.submitList(listOfProducts)
                    }
                }
            }
        }



        recyclerView = view.findViewById(R.id.recyclerView)

        communicator = activity as Communicator
        val adapterListener : (Product) -> Unit = {
            // move product to details fragment through communicator
            communicator.showProductDetails(it)
        }
        recyclerAdapter = ProductListAdapter(adapterListener, requireContext())
        recyclerAdapter.submitList(listOfProducts)


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