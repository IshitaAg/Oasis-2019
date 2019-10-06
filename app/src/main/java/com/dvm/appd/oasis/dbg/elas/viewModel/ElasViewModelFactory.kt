package com.dvm.appd.oasis.dbg.elas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.elas.ElasModule
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ElasViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var elasRepository: ElasRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newElasComponent(ElasModule()).injectElas(this)
        return ElasViewModel(elasRepository) as T
    }
}