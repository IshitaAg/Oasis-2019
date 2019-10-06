package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "misc_table")
data class MiscEventsData(

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    var id: String,

    @ColumnInfo(name = "event_name")
    var name: String,

    @ColumnInfo(name = "event_venue")
    var venue: String,

    @ColumnInfo(name = "event_time")
    var time: String,

    @ColumnInfo(name = "event_description")
    var description: String,

    @ColumnInfo(name = "event_day")
    var day: String,

    @ColumnInfo(name = "organiser")
    var organiser: String,

    @ColumnInfo(name = "favourite")
    var isFavourite: Int
)