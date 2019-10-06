package com.dvm.appd.oasis.dbg.di.splash

import com.dvm.appd.oasis.dbg.splash.viewmodel.SplashViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(factory:SplashViewModelFactory)
}