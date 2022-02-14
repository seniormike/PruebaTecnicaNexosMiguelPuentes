package com.mapr.credibanco.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataAuthorization(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var receiptId: String = "",
    var rrn: String = "",
    var statusCode: String = "",
    var statusDescription: String = "",
    var uuid: String = "",
    var commerceCode: String = "",
    var terminalCode: String = "",
    var amount: String = "",
    var card: String = ""
)