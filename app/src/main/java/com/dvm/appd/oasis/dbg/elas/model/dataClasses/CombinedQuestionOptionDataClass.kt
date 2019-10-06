package com.dvm.appd.oasis.dbg.elas.model.dataClasses

data class CombinedQuestionOptionDataClass(
    val questionId: Long,
    val option_id: Long,
    val option: String,
    val question: String,
    val category: String,
    val questionNumber: String
)