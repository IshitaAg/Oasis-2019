package com.dvm.appd.oasis.dbg.events.data.repo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.*
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.events.data.retrofit.AllEventsPojo
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventItemPojo
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsPojo
import com.dvm.appd.oasis.dbg.events.data.room.EventsDao
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsService
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.*
import com.google.common.util.concurrent.ListenableFuture
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

        //getEventsData().subscribe()

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = PeriodicWorkRequestBuilder<EventsSyncWorker>(2, TimeUnit.HOURS).setConstraints(constraints).build()

        WorkManager.getInstance(application.applicationContext).enqueue(request)

        db.collection("events").document("misc").collection("eventdata")
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    Log.e("EventsRepo", "Listen failed", exception)
                    return@addSnapshotListener
                }
                if (snapshot != null) {

                    var miscEvents: MutableList<MiscEventsData> = arrayListOf()

                    for (doc in snapshot.documentChanges) {

                        var name: String
                        var venue: String
                        var time: String
                        var description: String
                        var day: String
                        var organiser: String

                        when (doc.type) {

                            DocumentChange.Type.ADDED -> {
                                Log.d("DocAdded", doc.document.get("name").toString())

                                name = try {
                                    doc.document["name"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                venue = try {
                                    doc.document["venue"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                time = try {
                                    doc.document["timestamp"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                description = try {
                                    doc.document["description"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                day = try {
                                    doc.document["day"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                organiser = try {
                                    doc.document["organiser"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }

                                miscEvents.add(
                                    MiscEventsData(
                                        id = doc.document.id,
                                        name = name,
                                        venue = venue,
                                        time = time,
                                        description = description,
                                        day = day,
                                        organiser = organiser,
                                        isFavourite = 0
                                    )
                                )
                            }

                            DocumentChange.Type.MODIFIED -> {
                                Log.d("DocChanged", doc.document.get("name").toString())

                                name = try {
                                    doc.document["name"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                venue = try {
                                    doc.document["venue"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                time = try {
                                    doc.document["timestamp"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                description = try {
                                    doc.document["description"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                day = try {
                                    doc.document["day"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }
                                organiser = try {
                                    doc.document["organiser"].toString()
                                } catch (e: Exception) {
                                    "Not Available"
                                }

                                eventsDao.updateMiscData(
                                    id = doc.document.id,
                                    name = name,
                                    venue = venue,
                                    time = time,
                                    description = description,
                                    day = day,
                                    organiser = organiser
                                )
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({},{
                                        Log.e("EventsRepo", it.message, it)
                                    })
                            }

                            DocumentChange.Type.REMOVED -> {
                                Log.d("DocRemoved", doc.document.get("name").toString())
                                eventsDao.deleteMiscEvent(doc.document.id)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({},{
                                        Log.e("EventsRepo", it.message, it)
                                    })
                            }

                        }
                    }

                    Log.d("Events", miscEvents.toString())
//                    Completable.fromAction {
//                        eventsDao.updateAllMiscEvents(miscEvents)
//                    }.subscribeOn(Schedulers.io()).subscribe({},{})

                    eventsDao.insertMiscEventData(miscEvents).subscribeOn(Schedulers.io()).subscribe({},{})
                }
            }

    }

    fun miscEventDays(): Flowable<List<String>> {
        return eventsDao.getMiscDays().subscribeOn(Schedulers.io())
    }

    fun getDayMiscEvents(day: String): Flowable<List<MiscEventsData>> {
        return eventsDao.getDayMiscEvents(day).subscribeOn(Schedulers.io())
    }

    fun getEventsData(): Single<ListenableWorker.Result>{
        return eventsService.getAllEvents().subscribeOn(Schedulers.io())
            .flatMap {response ->

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

                        Log.d("NewEvents", "$events")
                        Log.d("NewEvents", "$categories")
                        Log.d("NewEvents", "$venues")
                        eventsDao.deleteAndInsertEvents(events)
                        eventsDao.deleteAndInsertCategories(categories)
                        eventsDao.deleteAndInsertVenues(venues)

                        workerResult = ListenableWorker.Result.success()
                    }

                    else -> {
                        workerResult = ListenableWorker.Result.retry()
                    }

                }
                return@flatMap Single.just(workerResult)
            }


    }

    fun EventItemPojo.toEventData(): EventData{

        return EventData(id, name, about, rules, time, dateTime, duration, image, details)
    }

    fun EventItemPojo.toCategoryData(): List<CategoryData>{

        val eventCategories: MutableList<CategoryData> = arrayListOf()

        categories.forEach {
            eventCategories.add(CategoryData(it, id, 0))
        }

        return eventCategories
    }

    fun EventItemPojo.toVenueData(): List<VenueData>{

        val eventVenues: MutableList<VenueData> = arrayListOf()

        eventVenues.add(VenueData(venue, id, 0))

        return eventVenues
    }

    fun updateMiscFavourite(eventId: String, favouriteMark: Int): Completable {
        return eventsDao.updateMiscFavourite(id = eventId, mark = favouriteMark)
            .subscribeOn(Schedulers.io())
    }
}





