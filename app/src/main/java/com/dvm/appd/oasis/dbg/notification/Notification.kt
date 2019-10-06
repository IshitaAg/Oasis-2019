package com.dvm.appd.oasis.dbg.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val channel: String
){
    override fun toString(): String {
        return "Id = ${this.id}\nTitle = ${this.title}\nBody = ${this.body}\nChannel = ${this.channel}"
    }
}