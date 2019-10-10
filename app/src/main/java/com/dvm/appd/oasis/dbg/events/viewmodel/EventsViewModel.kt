package com.dvm.appd.oasis.dbg.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class EventsViewModel(val eventsRepository: EventsRepository): ViewModel() {

    var miscEvents: LiveData<List<MiscEventsData>> = MutableLiveData()
    var eventDays: LiveData<List<String>> = MutableLiveData()
    var daySelected: LiveData<String> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    lateinit var currentSubsciption: Disposable

    init {

        eventsRepository.miscEventDays()
            .subscribe({
                Log.d("MiscEventVM", it.toString())
                (eventDays as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                Log.d("MiscEventVM", it.toString())
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun markEventFavourite(eventId: String, favouriteMark: Int){
        eventsRepository.updateMiscFavourite(eventId, favouriteMark).subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            if (favouriteMark == 1)
                (error as MutableLiveData).postValue("You will now receive notifications for this event")
            else if (favouriteMark == 0)
                (error as MutableLiveData).postValue("You will no longer receive notifications for this event")
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun getMiscEventsData(day: String){
        currentSubsciption = eventsRepository.getDayMiscEvents(day)
            .subscribe({
                (progressBarMark as MutableLiveData).postValue(1)
                Log.d("MiscEventVM", it.toString())
                (miscEvents as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                Log.d("MiscEventVM", it.toString())
                (progressBarMark as MutableLiveData).postValue(1)
                (error as MutableLiveData).postValue(it.message)
            })
    }

    fun refreshData(){
        eventsRepository.getEventsData().subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
        })
    }
}