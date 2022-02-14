package com.mapr.credibanco.services.responses

data class ResponseAuthorization(
    val receiptId: String,
    val rrn: String,
    val statusCode: String,
    val statusDescription: String
)