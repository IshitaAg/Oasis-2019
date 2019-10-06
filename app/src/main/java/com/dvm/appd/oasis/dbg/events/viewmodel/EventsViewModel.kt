package com.dvm.appd.oasis.dbg.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.EventsData

class EventsViewModel(val eventsRepository: EventsRepository): ViewModel() {

    var sportsName: LiveData<List<EventsData>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)

    init {

        eventsRepository.getSportsName()
        .subscribe({
            Log.d("EventRepo", it.toString())
            (sportsName as MutableLiveData).postValue(it)
            (error as MutableLiveData).postValue(null)
        },{
            Log.d("EventRepo", it.toString())
            (error as MutableLiveData).postValue(it.message)
        })

    }

    fun markFavourite(sport: String, favMark: Int){
        eventsRepository.updateEventFavourite(sport, favMark).subscribe({
            if (favMark == 1)
                (error as MutableLiveData).postValue("You will now receive notifications for this sport")
            else if (favMark == 0)
                (error as MutableLiveData).postValue("You will no longer receive notifications for this event")
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }
}