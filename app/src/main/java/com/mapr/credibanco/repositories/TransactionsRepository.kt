package com.mapr.credibanco.repositories

import android.app.Application
import com.google.gson.Gson
import com.mapr.credibanco.application.AppCredibanco
import com.mapr.credibanco.model.db.DataAuthorization
import com.mapr.credibanco.services.ApiServices
import com.mapr.credibanco.services.requests.RequestAuthorization
import com.mapr.credibanco.services.requests.RequestCancellation
import com.mapr.credibanco.services.responses.ResponseAuthorization
import com.mapr.credibanco.services.responses.ResponseCancellation
import com.mapr.credibanco.tools.Constants
import com.mapr.credibanco.tools.Constants.BASE_URL
import com.mapr.credibanco.tools.Utils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransactionsRepository(private val application: Application) {

    private var apiServices: ApiServices = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiServices::class.java)

    fun makeAuthorizationRequest(
        auth: String,
        requestAuthorization: RequestAuthorization,
        listener: OnListenerResponseAuthorization
    ) {
        val authorization = "Basic ${Utils().convertToBase64(auth)}"
        Utils().printLog("AUTH: " + Gson().toJson(authorization))
        Utils().printLog("makeAuth Request: " + Gson().toJson(requestAuthorization))

        val call: Call<ResponseAuthorization> =
            apiServices.makeAuthorization(authorization, requestAuthorization)
        call.enqueue(object : Callback<ResponseAuthorization> {
            override fun onResponse(
                call: Call<ResponseAuthorization>,
                response: Response<ResponseAuthorization>
            ) {
                val responseObj: ResponseAuthorization = response.body()!!
                Utils().printLog("makeAuth Response: " + Gson().toJson(responseObj))

                if (responseObj.statusCode == Constants.AUTH_APPROVED_STATUS) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dataTodb = DataAuthorization()
                        dataTodb.uuid = requestAuthorization.id
                        dataTodb.commerceCode = requestAuthorization.commerceCode
                        dataTodb.terminalCode = requestAuthorization.terminalCode
                        dataTodb.amount = requestAuthorization.amount
                        dataTodb.card = requestAuthorization.card
                        dataTodb.receiptId = responseObj.receiptId
                        dataTodb.rrn = responseObj.rrn
                        dataTodb.statusCode = responseObj.statusCode
                        dataTodb.statusDescription = responseObj.statusDescription
                        val insert = AppCredibanco(application).dataAuthDao().insertOne(dataTodb)
                        Utils().printLog("Insertado en la BD Auth $insert")

                    }
                }
                listener.onResponseAuth(responseObj)
            }

            override fun onFailure(call: Call<ResponseAuthorization>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedAuth()
            }
        })
    }

    /**
     *
     */
    interface OnListenerResponseAuthorization {
        fun onResponseAuth(responseAuthorization: ResponseAuthorization)
        fun onFailedAuth()
    }

    fun makeCancellationRequest(
        auth: String,
        requestCancellation: RequestCancellation,
        listener: OnListenerResponseCancellation
    ) {
        val authorization = "Basic ${Utils().convertToBase64(auth)}"
        Utils().printLog("AUTH: " + Gson().toJson(authorization))
        Utils().printLog("makeAuth Request: " + Gson().toJson(requestCancellation))

        val call: Call<ResponseCancellation> =
            apiServices.makeCancellation(authorization, requestCancellation)
        call.enqueue(object : Callback<ResponseCancellation> {
            override fun onResponse(
                call: Call<ResponseCancellation>,
                response: Response<ResponseCancellation>
            ) {
                val responseObj: ResponseCancellation = response.body()!!
                Utils().printLog("makeAuth Response: " + Gson().toJson(responseObj))

                if (responseObj.statusCode == Constants.AUTH_APPROVED_STATUS) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dataTodb =AppCredibanco(application).dataAuthDao().findByReceiptId(requestCancellation.receiptId)
                        AppCredibanco(application).dataAuthDao().delete(dataTodb)
                        Utils().printLog("Eliminado en la BD Auth $dataTodb")
                    }
                }
                listener.onResponseCancellation(responseObj)
            }

            override fun onFailure(call: Call<ResponseCancellation>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedCancellation()
            }
        })
    }

    /**
     *
     */
    interface OnListenerResponseCancellation {
        fun onResponseCancellation(responseCancellation: ResponseCancellation)
        fun onFailedCancellation()
    }

    companion object {
        @Volatile
        private var instance: TransactionsRepository? = null
        private val LOCK = Any()
        fun getInstance(context: Application) = instance ?: synchronized(LOCK) {
            instance ?: TransactionsRepository(context).also { instance = it }
        }
    }
}