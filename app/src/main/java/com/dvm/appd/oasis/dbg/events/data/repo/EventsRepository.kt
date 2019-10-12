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
import java.util.concurrent.TimeUnit


class EventsRepository(val eventsDao: EventsDao, val eventsService: EventsService, val application: Application,val comediansVoting: ComediansVoting,val sharedPreferences: SharedPreferences) {

    val db = FirebaseFirestore.getInstance()

    init {

        //updateEventsData().subscribe()

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = PeriodicWorkRequestBuilder<EventsSyncWorker>(1, TimeUnit.MINUTES).setConstraints(constraints).build()

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

                    else -> {
                        Log.d("NewEvents", "error")
                        workerResult = ListenableWorker.Result.retry()
                    }

                }
                return@flatMap Single.just(workerResult)
            }


    }

    private fun EventItemPojo.toEventData(): EventData{

        return if (time == "TBA" || dateTime == "TBA")
            EventData(eventId = id,name =  name, about = about, rules = rules, time = time, date = dateTime, duration = duration, image_url = image, details = details, venues = venue)
        else
            EventData(eventId = id,name =  name, about = about, rules = rules, time = time.substring(0, 5), date = dateTime.substring(0, 10), duration = duration, image_url = image, details = details, venues = venue)
    }

    private fun EventItemPojo.toCategoryData(): List<CategoryData>{

        val eventCategories: MutableList<CategoryData> = arrayListOf()

        categories.forEach {
            eventCategories.add(CategoryData(it, id, 0))
        }

        return eventCategories
    }

    fun updateFavourite(eventId: Int, favMark: Int): Completable{

        return if (favMark == 1){
            eventsDao.insertFavEvent(FavEvents(eventId, 1))
                .subscribeOn(Schedulers.io())
        } else{
            eventsDao.deleteFavEvent(eventId).subscribeOn(Schedulers.io())
        }
    }

    fun getEventsDayData(date: String): Flowable<List<ModifiedEventsData>>{

        return eventsDao.getAllEventsByDate(date).subscribeOn(Schedulers.io())
    }

    fun getEventsDayCategoryData(date: String, categories: List<String>): Flowable<List<ModifiedEventsData>>{

        return eventsDao.getEventsByCategory(date, categories).subscribeOn(Schedulers.io())
            .flatMap {

                var events: MutableList<ModifiedEventsData> = arrayListOf()

                for ((index, item) in it.listIterator().withIndex()){

                    if (index != it.lastIndex && it[index].eventId != it[index+1].eventId)
                        events.add(item)
                    else if (index == it.lastIndex)
                        events.add(item)
                }

                return@flatMap Flowable.just(events)
            }
    }

    fun getEventsDates(): Flowable<List<String>>{
        return eventsDao.getEventsDates().subscribeOn(Schedulers.io())
    }

    fun getCategories(): Flowable<List<String>>{
        return eventsDao.getAllCategories().subscribeOn(Schedulers.io())
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





