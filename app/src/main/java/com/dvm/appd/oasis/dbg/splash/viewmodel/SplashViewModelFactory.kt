package com.dvm.appd.oasis.dbg.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.di.splash.SplashModule
import javax.inject.Inject

class SplashViewModelFactory:ViewModelProvider.Factory {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newSplashComponent(SplashModule()).inject(this)
        return SplashViewModel(authRepository) as T
    }
}