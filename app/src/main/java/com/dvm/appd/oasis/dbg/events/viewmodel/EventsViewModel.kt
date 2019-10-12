package com.dvm.appd.oasis.dbg.events.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.ChildEventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.ModifiedEventsData
import io.reactivex.disposables.Disposable

@SuppressLint("CheckResult")
class EventsViewModel(val eventsRepository: EventsRepository): ViewModel() {

    var miscEvents: LiveData<List<MiscEventsData>> = MutableLiveData()
    var eventDays: LiveData<List<String>> = MutableLiveData()
    var daySelected: LiveData<String> = MutableLiveData()
    var events: LiveData<List<ModifiedEventsData>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    lateinit var currentSubscription: Disposable

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

        eventsRepository.getEventsData()
            .subscribe({
                Log.d("NewEvents", "RoomSuccess")
                (events as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                Log.d("NewEvents", "Room $it")
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun markEventFav(eventId: Int, favMark: Int){
        eventsRepository.updateFavourite(eventId, favMark).subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            if (favMark == 1)
                (error as MutableLiveData).postValue("You will now receive notifications for this event")
            else if (favMark == 0)
                (error as MutableLiveData).postValue("You will no longer receive notifications for this event")
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun getMiscEventsData(day: String){
        currentSubscription = eventsRepository.getDayMiscEvents(day)
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
        eventsRepository.updateEventsData().subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }
}