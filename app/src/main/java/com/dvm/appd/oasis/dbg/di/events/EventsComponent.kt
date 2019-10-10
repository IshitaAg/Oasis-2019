package com.dvm.appd.oasis.dbg.di.events

import com.dvm.appd.oasis.dbg.events.viewmodel.MiscEventsViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EventsModule::class])
interface EventsComponent {

    fun injectMiscEvents(factory: MiscEventsViewModelFactory)

}