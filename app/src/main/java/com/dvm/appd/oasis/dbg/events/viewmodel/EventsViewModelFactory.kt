package com.dvm.appd.oasis.dbg.events.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.events.EventsModule
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import javax.inject.Inject

class EventsViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var eventsRepository: EventsRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newEventsComponent(EventsModule()).injectMiscEvents(this)
        @Suppress("UNCHECKED_CAST")
        return EventsViewModel(eventsRepository) as T
    }
}