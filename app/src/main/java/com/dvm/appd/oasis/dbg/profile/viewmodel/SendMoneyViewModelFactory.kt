package com.dvm.appd.oasis.dbg.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.profile.ProfileModule
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import javax.inject.Inject

class SendMoneyViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var walletRepository: WalletRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newProfileComponent(ProfileModule()).inject(this)
        return SendMoneyViewModel(walletRepository) as T
    }
}