package com.dvm.appd.oasis.dbg.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.more.MoreModule

class VotingViewModelFactory:ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newMoreComponent(MoreModule()).inject(this)
        return VotingViewModel() as T
    }
}