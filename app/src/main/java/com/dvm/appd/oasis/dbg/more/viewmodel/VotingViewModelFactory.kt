package com.dvm.appd.oasis.dbg.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.di.more.MoreModule
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.more.data.MoreRepository
import javax.inject.Inject

class VotingViewModelFactory:ViewModelProvider.Factory {

    @Inject
    lateinit var moreRepository: MoreRepository

    @Inject
    lateinit var authRepository: AuthRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newMoreComponent(MoreModule()).inject(this)
        return VotingViewModel(moreRepository,authRepository) as T
    }
}