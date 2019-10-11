package com.dvm.appd.oasis.dbg.di.more

import com.dvm.appd.oasis.dbg.more.viewmodel.VotingViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [MoreModule::class])
interface MoreComponent {

    fun inject(factory:VotingViewModelFactory)
}