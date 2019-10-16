package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_categories")
data class CategoryData(

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "event_id")
    val eventId: Int,

    @ColumnInfo(name = "filtered")
    val isFiltered: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int
)