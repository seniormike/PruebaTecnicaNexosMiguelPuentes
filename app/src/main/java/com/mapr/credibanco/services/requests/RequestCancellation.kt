package com.mapr.credibanco.services.requests

import com.google.gson.annotations.SerializedName

class RequestCancellation(
    @SerializedName("receiptId") var receiptId: String,
    @SerializedName("rrn") var rrn: String
)