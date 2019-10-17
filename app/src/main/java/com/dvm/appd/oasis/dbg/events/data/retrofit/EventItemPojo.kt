package com.dvm.appd.oasis.dbg.events.data.retrofit

import com.google.gson.annotations.SerializedName

data class EventItemPojo(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("rules")
    val rules: String,

    @SerializedName("timings")
    val timing: String,

    @SerializedName("date_time")
    val dateTime: String,

    @SerializedName("categories")
    val categories: List<String>,

    @SerializedName("venue")
    val venue: String,

    @SerializedName("details")
    val details: String
)