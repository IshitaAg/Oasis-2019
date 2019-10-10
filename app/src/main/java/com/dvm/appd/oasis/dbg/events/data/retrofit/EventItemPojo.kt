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

    @SerializedName("time")
    val time: String,

    @SerializedName("date_time")
    val dateTime: String,

    @SerializedName("categories")
    val categories: List<String>,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("venue")
    val venue: String,

    @SerializedName("image_url")
    val image: String,

    @SerializedName("details")
    val details: String
)