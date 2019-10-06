package com.dvm.appd.oasis.dbg.elas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.elas.ElasModule
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import javax.inject.Inject

class ElasQuestionViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var elasRepository: ElasRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newElasComponent(ElasModule()).injectElasQuestion(this)
        return ElasQuestionViewModel(elasRepository) as T
    }
}