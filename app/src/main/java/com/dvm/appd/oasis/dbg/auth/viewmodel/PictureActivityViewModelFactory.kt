package com.dvm.appd.oasis.dbg.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.di.auth.AuthModule
import javax.inject.Inject

class PictureActivityViewModelFactory:ViewModelProvider.Factory {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newAuthComponent(AuthModule()).inject(this)
        return PictureActivityViewModel(authRepository) as T
    }
}