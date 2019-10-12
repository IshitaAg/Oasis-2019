package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_data")
data class EventData(

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val eventId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "about")
    val about: String,

    @ColumnInfo(name = "rules")
    val rules: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "details")
    val details: String,

    @ColumnInfo(name = "venue")
    val venues: String
)