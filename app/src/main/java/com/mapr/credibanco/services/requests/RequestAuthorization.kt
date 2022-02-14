package com.mapr.credibanco.services.requests

import com.google.gson.annotations.SerializedName

class RequestAuthorization(
    @SerializedName("id") var id: String,
    @SerializedName("commerceCode") var commerceCode: String,
    @SerializedName("terminalCode") var terminalCode: String,
    @SerializedName("amount") var amount: String,
    @SerializedName("card") var card: String
)