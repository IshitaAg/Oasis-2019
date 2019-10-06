package com.dvm.appd.oasis.dbg.wallet.views

sealed class StallResult {
    object Failure:StallResult()
    object Success:StallResult()
}