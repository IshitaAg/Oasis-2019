package com.dvm.appd.oasis.dbg.elas.model.retrofit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ranking_table")
data class PlayerRankingResponse(
    val Name: String,
    val Points: Int,
    @PrimaryKey val Rank: Int
)