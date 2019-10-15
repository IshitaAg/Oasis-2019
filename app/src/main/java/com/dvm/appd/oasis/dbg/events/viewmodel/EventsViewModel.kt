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
    var daySelected: LiveData<String> = MutableLiveData("2019-10-19")
    var events: LiveData<List<ModifiedEventsData>> = MutableLiveData()
    var categories: LiveData<List<FilterData>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    var currentSubscription: Disposable? = null

    init {
        eventsRepository.getEventsDates()
            .subscribe({
                (eventDays as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })
    }

    fun getEventData(date: String){

        currentSubscription?.dispose()

        currentSubscription = eventsRepository.getCategories()
            .doOnNext {
                (categories as MutableLiveData).postValue(it)
            }
            .switchMap {
                if (it.firstOrNull { it.filtered } == null){
                    return@switchMap eventsRepository.getEventsDayData(date)
                }
                else {
                    return@switchMap eventsRepository.getEventsDayCategoryData(date)
                }
            }
            .subscribe({
                (progressBarMark as MutableLiveData).postValue(1)
                (events as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                Log.d("Categories", it.message)
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun refreshData(){
        eventsRepository.updateEventsData().subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
            getEventData(daySelected.value.toString())
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

    override fun onCleared() {
        super.onCleared()
        currentSubscription?.dispose()
    }
}