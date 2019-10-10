package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_data")
data class FavEvents(

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val eventId: Int,

    @ColumnInfo(name = "is_fav")
    val favMark: Int
)