package com.mapr.credibanco.services

import com.mapr.credibanco.services.requests.RequestAuthorization
import com.mapr.credibanco.services.requests.RequestCancellation
import com.mapr.credibanco.services.responses.ResponseAuthorization
import com.mapr.credibanco.services.responses.ResponseCancellation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServices {

    @POST("authorization")
    @Headers("Accept:Application/json")
    fun makeAuthorization(
        @Header("Authorization") auth: String,
        @Body data: RequestAuthorization
    ): Call<ResponseAuthorization>

    @POST("annulment")
    @Headers("Accept:Application/json")
    fun makeCancellation(
        @Header("Authorization") auth: String,
        @Body data: RequestCancellation
    ): Call<ResponseCancellation>
}