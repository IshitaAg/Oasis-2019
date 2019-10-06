package com.dvm.appd.oasis.dbg.di.elas

import com.dvm.appd.oasis.dbg.elas.viewModel.ElasQuestionViewModelFactory
import com.dvm.appd.oasis.dbg.elas.viewModel.ElasViewModelFactory
import com.dvm.appd.oasis.dbg.elas.viewModel.RulesDialogViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [ElasModule::class])
interface ElasComponent  {

    fun injectElas(factory: ElasViewModelFactory)
    fun injectElasQuestion(factory: ElasQuestionViewModelFactory)
    fun injectElasRules(factory: RulesDialogViewModelFactory)
}