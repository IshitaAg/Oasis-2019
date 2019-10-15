package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paytm_transactions")
data class PaytmRoom(

    val status: String,

    val checkSumHash: String,

    val bankName: String,

    val orderId: String,

    val txnAmount: String,

    val txnDate: String,

    val mid: String,

    @PrimaryKey
    val txnId: String,

    val respCode: String,

    val paymentMode: String,

    val bankTxnId: String,

    val currency: String,

    val gatewayName: String,

    val respMsg: String

)