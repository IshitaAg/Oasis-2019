package com.dvm.appd.oasis.dbg.events.data.retrofit

import com.google.gson.annotations.SerializedName

data class AllEventsPojo(

    @SerializedName("data")
    val events: List<EventsPojo>
)