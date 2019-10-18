package com.dvm.appd.oasis.dbg.events.data.repo

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventItemPojo
import com.dvm.appd.oasis.dbg.events.data.room.EventsDao
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsService
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.*
import com.dvm.appd.oasis.dbg.more.ComediansVoting
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import com.google.firebase.firestore.DocumentChange

import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.TimeUnit


class EventsRepository(val eventsDao: EventsDao, val eventsService: EventsService, val application: Application,val comediansVoting: ComediansVoting,val sharedPreferences: SharedPreferences) {

    init {

        updateEventsData().subscribe()

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = PeriodicWorkRequestBuilder<EventsSyncWorker>(3, TimeUnit.HOURS).setConstraints(constraints).build()

        WorkManager.getInstance(application.applicationContext).enqueue(request)

    }

    fun updateEventsData(): Single<ListenableWorker.Result>{
        return eventsService.getAllEvents().subscribeOn(Schedulers.io())
            .flatMap {response ->

                Log.d("NewEvents", "${response.code()}")
                val workerResult: ListenableWorker.Result

                when(response.code()){

                    200 -> {

                        var events: MutableList<EventData> = arrayListOf()
                        var categories: MutableList<CategoryData> = arrayListOf()

                        response.body()!!.events.forEach {eventsPojo ->

                            eventsPojo.events.forEach {
                                events.add(it.toEventData())
                                categories.addAll(it.toCategoryData())
                            }
                        }

                        eventsDao.deleteAndInsertEvents(events)
                        eventsDao.deleteAndInsertCategories(categories)

                        workerResult = ListenableWorker.Result.success()
                    }

                    500 ->
                    {
                        throw Exception("Error occured!!! Contact DVM official")
                    }
                    else -> {
                        var errorBody: String?
                        try {
                            errorBody = response.errorBody()?.string()

                        } catch (e: Exception) {
                            throw Exception("Code:${response.code()} Something went wrong!!!")
                        }
                        if (errorBody.isNullOrBlank()) {
                            throw Exception("Code: (${response.code()} Unknown Error Occured")
                        }

                        else {
                            val json = JSONObject(errorBody)
                            when {
                                json.has("display_message") -> {
                                    throw Exception("Code" + response.code() + json.getString("display_message"))
                                }
                                json.has("detail") -> throw Exception("Code" + json.getString("detail"))

                                else -> throw Exception("Code: ${response.code()}: Unknown error occurred")
                            }

                        }
                    }

                }
                return@flatMap Single.just(workerResult)
            }


    }

    private fun EventItemPojo.toEventData(): EventData{

        return if (timing == "TBA" || dateTime == "TBA")
            EventData(eventId = id,name =  name, about = about, rules = rules, time = timing, date = dateTime, details = details, venues = venue, contact = contact.ifBlank { "NA" })
        else
            EventData(eventId = id,name =  name, about = about, rules = rules, time = timing, date = dateTime.substring(0, 10), details = details, venues = venue, contact = contact.ifBlank { "NA" })
    }

    private fun EventItemPojo.toCategoryData(): List<CategoryData>{

        val eventCategories: MutableList<CategoryData> = arrayListOf()

        categories.forEach {
            eventCategories.add(CategoryData(it, id, false, 0))
        }

        return eventCategories
    }

//    fun updateFavourite(eventId: Int, favMark: Int): Completable{
//
//        return if (favMark == 1){
//            eventsDao.insertFavEvent(FavEvents(eventId, 1))
//                .subscribeOn(Schedulers.io())
//        } else{
//            eventsDao.deleteFavEvent(eventId).subscribeOn(Schedulers.io())
//        }
//    }

    fun getEventsDayData(date: String): Flowable<List<ModifiedEventsData>>{

        return eventsDao.getAllEventsByDate(date).subscribeOn(Schedulers.io())
            .switchMap {
                return@switchMap Flowable.just(it.sortedBy { data -> data.time })
            }
    }

    fun getEventsDayCategoryData(date: String): Flowable<List<ModifiedEventsData>>{

        return eventsDao.getEventsByCategory(date).subscribeOn(Schedulers.io())
            .switchMap {

                var events: MutableList<ModifiedEventsData> = arrayListOf()

                for ((index, item) in it.listIterator().withIndex()){

                    if (index != it.lastIndex && it[index].eventId != it[index+1].eventId)
                        events.add(item)
                    else if (index == it.lastIndex)
                        events.add(item)
                }

                events.sortBy { data -> data.time }

                return@switchMap Flowable.just(events)
            }
    }

    fun getEventsDates(): Flowable<List<String>>{
        return eventsDao.getEventsDates().subscribeOn(Schedulers.io())
    }

    fun getCategories(): Flowable<List<FilterData>>{
        return eventsDao.getAllCategories().subscribeOn(Schedulers.io())
    }

    fun updateFilter(category: String, filtered: Boolean): Completable{
        return eventsDao.updateFiltered(category, filtered).subscribeOn(Schedulers.io())
    }

    fun removeFilters(): Completable{
        return eventsDao.removeFilters().subscribeOn(Schedulers.io())
    }

    fun isVotingEnabled(): Flowable<Boolean> {
        return comediansVoting.getStatus()
    }

    fun getComedians():Flowable<List<Comedian>>{
        return comediansVoting.getComedians()
    }

    fun voteForComedian(name:String):Completable{
        return comediansVoting.vote(name).doOnComplete {
            sharedPreferences.edit().putBoolean(AuthRepository.Keys.voted,true).apply()
        }.doOnError {
            Log.d("checke",it.toString())
        }
    }
}





