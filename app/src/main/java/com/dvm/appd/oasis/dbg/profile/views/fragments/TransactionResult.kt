package com.dvm.appd.oasis.dbg.profile.views.fragments

sealed class TransactionResult {
    object Success: TransactionResult()
    data class Failure(val message:String):
        TransactionResult()
}