package com.dvm.appd.oasis.dbg.events.data.retrofit

import com.google.gson.annotations.SerializedName

data class EventsPojo(

    @SerializedName("events")
    val events: List<EventItemPojo>
)