package com.example.task_01

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListOfProductsFragments : Fragment(){
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: ProductListAdapter
    lateinit var communicator : Communicator
    private var listOfProducts : List<Product> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.list_frag, container, false)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<MyWorker>().setConstraints(constraints).build()

        WorkManager.getInstance(requireContext()).enqueue(request)

        val result : LiveData<WorkInfo> = WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.id)

        result.observe(viewLifecycleOwner) { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.FAILED -> {
                    Log.i("TAG", "onCreate: FAILED")
                }
                WorkInfo.State.SUCCEEDED -> {
                    Log.i("TAG", "onCreate: SUCCEEDED")
                    var jsonStr = workInfo.outputData.getString("ProductsJson")
                    if (jsonStr != null) {
                        val type = object : TypeToken<List<Product>>() {}.type
                        listOfProducts = Gson().fromJson(jsonStr, type)
                        recyclerAdapter.submitList(listOfProducts)
                    }
                }
                WorkInfo.State.ENQUEUED -> {}
                WorkInfo.State.RUNNING -> {}
                WorkInfo.State.BLOCKED -> {}
                WorkInfo.State.CANCELLED -> {}
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