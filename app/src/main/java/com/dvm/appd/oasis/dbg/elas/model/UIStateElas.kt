package com.dvm.appd.oasis.dbg.elas.model

import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass

sealed class UIStateElas {
    object Loading: UIStateElas()
    data class Failure(val message: String): UIStateElas()
    data class Questions(val questions: Map<Long, List<CombinedQuestionOptionDataClass>>): UIStateElas()
}