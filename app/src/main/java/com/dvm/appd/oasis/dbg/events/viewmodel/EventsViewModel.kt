package com.dvm.appd.oasis.dbg.events.viewmodel

import android.annotation.SuppressLint
import android.telephony.euicc.DownloadableSubscription
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.CategoryData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.FilterData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.ModifiedEventsData
import io.reactivex.disposables.Disposable

@SuppressLint("CheckResult")
class EventsViewModel(val eventsRepository: EventsRepository): ViewModel() {

    var eventDays: LiveData<List<String>> = MutableLiveData()
    var daySelected: LiveData<String> = MutableLiveData()
    var events: LiveData<List<ModifiedEventsData>> = MutableLiveData()
    var categories: LiveData<List<FilterData>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    lateinit var currentSubscription: Disposable

    init {

        eventsRepository.getEventsDates()
            .subscribe({
                (eventDays as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })

        eventsRepository.getCategories()
            .subscribe({
                (categories as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun getEventData(date: String){

        if (categories.value!!.firstOrNull { it.filtered } == null){
            currentSubscription = eventsRepository.getEventsDayData(date)
                .subscribe({
                    Log.d("NewEvents", "RoomSuccess")
                    (progressBarMark as MutableLiveData).postValue(1)
                    (events as MutableLiveData).postValue(it)
                    (error as MutableLiveData).postValue(null)
                },{
                    Log.d("NewEvents", "Room $it")
                    (progressBarMark as MutableLiveData).postValue(1)
                    (error as MutableLiveData).postValue(it.message)
                })
        }
        else {
            currentSubscription = eventsRepository.getEventsDayCategoryData(date)
                .subscribe({
                    Log.d("NewEvents", "RoomSuccess")
                    (progressBarMark as MutableLiveData).postValue(1)
                    (events as MutableLiveData).postValue(it)
                    (error as MutableLiveData).postValue(null)
                },{
                    Log.d("NewEvents", "Room $it")
                    (progressBarMark as MutableLiveData).postValue(1)
                    (error as MutableLiveData).postValue(it.message)
                })
        }

    }

//    fun markEventFav(eventId: Int, favMark: Int){
//        eventsRepository.updateFavourite(eventId, favMark).subscribe({
//            (progressBarMark as MutableLiveData).postValue(1)
//            if (favMark == 1)
//                (error as MutableLiveData).postValue("You will now receive notifications for this event")
//            else if (favMark == 0)
//                (error as MutableLiveData).postValue("You will no longer receive notifications for this event")
//        },{
//            (progressBarMark as MutableLiveData).postValue(1)
//            (error as MutableLiveData).postValue(it.message)
//        })
//    }

    fun refreshData(){
        eventsRepository.updateEventsData().subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun updatedFilters(category: String, filtered: Boolean){
        eventsRepository.updateFilter(category, filtered).subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }
}