package com.example.task_01

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson

@SuppressLint("RestrictedApi")
class MyWorker(private val appContext : Context, private val workerParams : WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (runAttemptCount == 5) {
            return Result.failure()
        }
        val client : ProductClient? = ProductClient.instance
        var listOfProducts : List<Product>? = client?.makeNetworkCall()
        if (listOfProducts.isNullOrEmpty()) {
            return Result.retry()
        }
        else {
            val json = Gson().toJson(listOfProducts)
            return Result.success(workDataOf("ProductsJson" to json))
        }

    }
}