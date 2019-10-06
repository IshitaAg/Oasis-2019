package com.dvm.appd.oasis.dbg.elas.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class QuestionData(
    @PrimaryKey val questionId: Long,
    val question: String,
    val category: String,
    val questionNumber: String
)


