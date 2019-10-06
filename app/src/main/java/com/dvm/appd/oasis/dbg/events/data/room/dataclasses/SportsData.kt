package com.dvm.appd.oasis.dbg.events.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports_table")
data class SportsData(

    @PrimaryKey
    @ColumnInfo(name = "match_no")
    var match_no: Int,

    @ColumnInfo(name = "layout")
    var layout: Int,

    @ColumnInfo(name = "gender")
    var gender: String,

    @ColumnInfo(name = "sport_name")
    var name: String,

    @ColumnInfo(name = "venue")
    var venue: String,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "round")
    var round: String,

    @ColumnInfo(name = "round_type")
    var round_type: String,

    @ColumnInfo(name = "team_1")
    var team_1: String,

    @ColumnInfo(name = "team_2")
    var team_2: String,

    @ColumnInfo(name = "is_score")
    var isScore: Boolean,

    @ColumnInfo(name = "score_1")
    var score_1: String,

    @ColumnInfo(name = "score_2")
    var score_2: String,

    @ColumnInfo(name = "winner_1")
    var winner1: String,

    @ColumnInfo(name = "winner_2")
    var winner2: String,

    @ColumnInfo(name = "winner_3")
    var winner3: String,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Int

)