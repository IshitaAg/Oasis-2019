package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_venues")
data class VenueData(

    @ColumnInfo(name = "venue")
    val venue: String,

    @ColumnInfo(name = "event_id")
    val eventId: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int
)