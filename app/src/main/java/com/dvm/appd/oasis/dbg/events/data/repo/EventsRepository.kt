package com.dvm.appd.oasis.dbg.events.data.repo

import android.app.Application
import android.util.Log
import androidx.work.*
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventItemPojo
import com.dvm.appd.oasis.dbg.events.data.room.EventsDao
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsService
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.*
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import com.google.firebase.firestore.DocumentChange

import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit


class EventsRepository(val eventsDao: EventsDao, val eventsService: EventsService, val application: Application) {

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
                        var venues: MutableList<VenueData> = arrayListOf()

                        response.body()!!.events.forEach {eventsPojo ->

                            eventsPojo.events.forEach {
                                events.add(it.toEventData())
                                categories.addAll(it.toCategoryData())
                                venues.addAll(it.toVenueData())
                            }
                        }

                        eventsDao.deleteAndInsertEvents(events)
                        eventsDao.deleteAndInsertCategories(categories)
                        eventsDao.deleteAndInsertVenues(venues)

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

        return EventData(id, name, about, rules, time, dateTime, duration, image, details)
    }

    private fun EventItemPojo.toCategoryData(): List<CategoryData>{

        val eventCategories: MutableList<CategoryData> = arrayListOf()

        categories.forEach {
            eventCategories.add(CategoryData(it, id, 0))
        }

        return eventCategories
    }

    private fun EventItemPojo.toVenueData(): List<VenueData>{

        val eventVenues: MutableList<VenueData> = arrayListOf()

        val venues = venue.split("~")
        venues.forEach {
            eventVenues.add(VenueData(it, id, 0))
        }

        return eventVenues
    }

    fun updateFavourite(eventId: Int, favMark: Int): Completable{

        return if (favMark == 1){
            eventsDao.insertFavEvent(FavEvents(eventId, 1))
                .subscribeOn(Schedulers.io())
        } else{
            eventsDao.deleteFavEvent(eventId).subscribeOn(Schedulers.io())
        }
    }

    fun getEventsData(): Flowable<List<ModifiedEventsData>>{

        return eventsDao.getAllEvents().subscribeOn(Schedulers.io())
            .flatMap {

                var events: MutableList<ModifiedEventsData> = arrayListOf()
                var categories: MutableList<String> = arrayListOf()
                var venues: MutableList<String> = arrayListOf()

                for ((index, item) in it.listIterator().withIndex()){

                    categories.add(item.category)
                    venues.add(item.venue)

                    if (index != it.lastIndex && it[index].eventId != it[index+1].eventId){

                        events.add(ModifiedEventsData(item.eventId, item.name, item.about, item.rules, item.time, item.dateTime, item.duration, item.imageUrl, item.details, venues, categories, item.isFav))
                        categories = arrayListOf()
                        venues = arrayListOf()
                    }
                    else if (index == it.lastIndex){
                        events.add(ModifiedEventsData(item.eventId, item.name, item.about, item.rules, item.time, item.dateTime, item.duration, item.imageUrl, item.details, venues, categories, item.isFav))
                        categories = arrayListOf()
                        venues = arrayListOf()
                    }
                }

               return@flatMap Flowable.just(events)
            }
    }
}





