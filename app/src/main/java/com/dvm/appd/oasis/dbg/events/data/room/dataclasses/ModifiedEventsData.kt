package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

data class ModifiedEventsData(

    val eventId: Int,

    val name: String,

    val about: String,

    val rules: String,

    val time: String,

    val dateTime: String,

    val duration: String,

    val imageUrl: String,

    val details: String,

    val venue: String,

    val category: String,

    val isFav: Int
)