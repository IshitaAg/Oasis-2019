package com.dvm.appd.oasis.dbg.elas.model.retrofit

import com.google.gson.annotations.SerializedName

data class RulesResponse(
    @SerializedName("rounds")
    val rules: List<Rule>
)

data class Rule(
    @SerializedName("round_name")
    val roundName: String,

    @SerializedName("round_no")
    val roundId: String,

    @SerializedName("rules")
    val rules_text: List<String>
)