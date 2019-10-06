package com.dvm.appd.oasis.dbg.events.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.SportsData

class SportsDataViewModel(val eventsRepository: EventsRepository, private var name: String) : ViewModel() {

    var sportsData: LiveData<Map<String,List<SportsData>>> = MutableLiveData()
    var gender: LiveData<List<String>> = MutableLiveData()
    var filter: LiveData<String> = MutableLiveData("Upcoming")
    var error: LiveData<String> = MutableLiveData(null)
    var epcIsABitch: LiveData<Pair<String, String>> = MutableLiveData(null)

    init {
        data()
    }

    @SuppressLint("CheckResult")
    private fun data() {
        eventsRepository.getGenderForSport(name).subscribe({
            Log.d("Sports6","Sucess getting gender room")
            (gender as MutableLiveData).postValue(it)
            (error as MutableLiveData).postValue(null)

        },{
            Log.d("Sports5","Error getting gender room")
            (error as MutableLiveData).postValue(it.message)
        })

        eventsRepository.getSportData(name).subscribe({
            Log.d("Sports6","Sucess getting sports data  room")
            (sportsData as MutableLiveData).postValue(it.groupBy { it.gender})
            (error as MutableLiveData).postValue(null)
        },{
            Log.d("Sports5","Error getting sportdata room")
            (error as MutableLiveData).postValue(it.message)
        })
    }

    @SuppressLint("CheckResult")
    fun markMatchFavourite(matchNo: Int, favouriteMark: Int){
        eventsRepository.updateSportsFavourite(matchNo, favouriteMark).subscribe({
            Log.d("SportsViewModel", "Changes in Room Complete")
            if (favouriteMark == 1)
                (error as MutableLiveData).postValue("You will now receive notifications for this sport")
            else if (favouriteMark == 0)
                (error as MutableLiveData).postValue("You will no longer receive notifications for this event")
        },{
            Log.e("SportsViewModel", "Crashed = ${it.toString()}")
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun getEpcData(eventId: String){
        eventsRepository.getEventEpc("sport", eventId).subscribe({
            (epcIsABitch as MutableLiveData).postValue(it)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }
}