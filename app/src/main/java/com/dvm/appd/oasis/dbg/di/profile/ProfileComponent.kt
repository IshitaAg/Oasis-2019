package com.dvm.appd.oasis.dbg.di.profile


import com.dvm.appd.oasis.dbg.profile.viewmodel.AddMoneyViewModelFactory
import com.dvm.appd.oasis.dbg.profile.viewmodel.ProfileViewModelFactory
import com.dvm.appd.oasis.dbg.profile.viewmodel.SendMoneyViewModelFactory
import com.dvm.appd.oasis.dbg.profile.viewmodel.TicketViewModelFactory
import dagger.Subcomponent


@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    fun inject(factory:ProfileViewModelFactory)
    fun inject(factory:AddMoneyViewModelFactory)
    fun inject(factory: SendMoneyViewModelFactory)
    fun injectTickets(factory: TicketViewModelFactory)
}