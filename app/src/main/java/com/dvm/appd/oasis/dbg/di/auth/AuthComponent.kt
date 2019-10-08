package com.dvm.appd.oasis.dbg.di.auth

import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModelFactory

import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(factory:AuthViewModelFactory)
}