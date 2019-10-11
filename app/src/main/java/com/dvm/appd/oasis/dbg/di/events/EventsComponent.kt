package com.dvm.appd.oasis.dbg.di.events

import com.dvm.appd.oasis.dbg.events.data.repo.EventsSyncWorker
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EventsModule::class])
interface EventsComponent {

    fun injectMiscEvents(factory: EventsViewModelFactory)
    fun injectSync(sync: EventsSyncWorker)

}