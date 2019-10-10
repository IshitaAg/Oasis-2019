package com.dvm.appd.oasis.dbg.di.auth

import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModelFactory
import com.dvm.appd.oasis.dbg.auth.viewmodel.PictureActivityViewModel
import com.dvm.appd.oasis.dbg.auth.viewmodel.PictureActivityViewModelFactory

import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(factory:AuthViewModelFactory)
    fun inject(factory:PictureActivityViewModelFactory)
}