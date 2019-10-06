package com.dvm.appd.oasis.dbg.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.di.auth.AuthModule
import javax.inject.Inject

class AuthViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var authRepository: AuthRepository


    override fun <T : ViewModel?> create(p0: Class<T>): T {
        OASISApp.appComponent.newAuthComponent(AuthModule()).inject(this)
        return AuthViewModel(authRepository) as T
    }
}