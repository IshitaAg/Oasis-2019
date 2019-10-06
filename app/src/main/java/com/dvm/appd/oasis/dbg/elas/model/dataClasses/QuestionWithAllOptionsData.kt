package com.dvm.appd.oasis.dbg.elas.model.dataClasses

import com.dvm.appd.oasis.dbg.elas.model.room.OptionData

data class QuestionWithAllOptionsData(
    val question: String,
    val category: String,
    val options: List<OptionData>
)