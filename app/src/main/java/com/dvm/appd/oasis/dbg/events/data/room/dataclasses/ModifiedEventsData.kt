package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

data class ModifiedEventsData(

    val eventId: Int,

    val name: String,

    val about: String,

    val rules: String,

    val time: String,

    val date: String,

    val details: String,

    val venue: String,

    val isFav: Int,

    val contact: String
)