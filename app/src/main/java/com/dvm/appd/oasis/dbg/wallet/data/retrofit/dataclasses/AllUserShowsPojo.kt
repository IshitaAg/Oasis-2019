package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class AllUserShowsPojo(

    @SerializedName("shows")
    val shows: List<UserShowPojo>
)