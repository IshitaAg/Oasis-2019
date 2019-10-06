package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventsData(

    @ColumnInfo(name = "event")
    @PrimaryKey
    var event: String,

    @ColumnInfo(name = "is_fav")
    var isFav: Int = 0
)