package com.dvm.appd.oasis.dbg.events.data.repo

import android.annotation.SuppressLint
import android.util.Log
import com.dvm.appd.oasis.dbg.events.data.room.EventsDao
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.EventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.SportsData
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsService
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import com.google.firebase.firestore.DocumentChange

import io.reactivex.Completable
import io.reactivex.Single


class EventsRepository(val eventsDao: EventsDao, val eventsService: EventsService) {

    val db = FirebaseFirestore.getInstance()

    init {

        getSportsDataFromFirestore()

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

    fun getSportsName(): Flowable<List<EventsData>> {
        return eventsDao.getSportsName().subscribeOn(Schedulers.io())

    }

    fun miscEventDays(): Flowable<List<String>> {
        return eventsDao.getMiscDays().subscribeOn(Schedulers.io())
    }

    fun getDayMiscEvents(day: String): Flowable<List<MiscEventsData>> {
        return eventsDao.getDayMiscEvents(day).subscribeOn(Schedulers.io())
    }

    fun getSportData(name: String): Flowable<List<SportsData>> {
        return eventsDao.getSportDataForSport(name).subscribeOn(Schedulers.io())
    }

    fun getGenderForSport(name: String): Flowable<List<String>> {
        return eventsDao.getDistinctGenderForSport(name).subscribeOn(Schedulers.io())
    }

    private fun getSportsDataFromFirestore() {

        db.collection("events").document("sports").collection("matches")
            .addSnapshotListener { snapshots, e ->

                if (e != null) {
                    Log.w("Sports", "listen:error", e)
                    return@addSnapshotListener
                }

                var sportsData: MutableList<SportsData> = arrayListOf()
                var sportsName: MutableList<EventsData> = arrayListOf()

                var match_no: Int
                var name: String
                var round: String
                var round_type: String
                var team_1: String
                var team_2: String
                var time_stamp: String
                var venue: String
                var gender: String
                var isScore: Boolean
                var layout: Int
                var score_1: String
                var score_2: String
                var winner1: String
                var winner2: String
                var winner3: String

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            Log.d("sports1", "added doc data : ${dc.document.data}")

                            match_no = try {
                                dc.document.id.toInt()
                            } catch (e: Exception) {
                                0
                            }
                            name = try {
                                dc.document["sport"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            round = try {
                                dc.document["round_name"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            round_type = try {
                                dc.document["round_type"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            team_1 = try {
                                dc.document["team1"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            team_2 = try {
                                dc.document["team2"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            time_stamp = try {
                                dc.document["timestamp"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            venue = try {
                                dc.document["venue"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }

                            gender = try {
                                dc.document["gender"].toString()
                            } catch (e: Exception) {
                                "Boys"
                            }
                            isScore = try {
                                dc.document["is_score"] as Boolean
                            } catch (e: Exception) {
                                false
                            }
                            layout = try {
                                (dc.document["layout"] as Long).toInt()
                            } catch (e: Exception) {
                                2
                            }
                            score_1 = try {
                                dc.document["score1"].toString()
                            } catch (e: Exception) {
                                Log.e("sports1", "Exception = ${e.toString()}")
                                "Not Available"
                            }
                            score_2 = try {
                                dc.document["score2"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            winner1 = try {
                                dc.document["winner1"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            winner2 = try {
                                dc.document["winner2"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            winner3 = try {
                                dc.document["winner3"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }

                            sportsData.add(
                                SportsData(
                                    match_no = match_no,
                                    name = name,
                                    round = round,
                                    round_type = round_type,
                                    team_1 = team_1,
                                    team_2 = team_2,
                                    time = time_stamp,
                                    venue = venue,
                                    gender = gender,
                                    isScore = isScore,
                                    layout = layout,
                                    score_1 = score_1,
                                    score_2 = score_2,
                                    winner1 = winner1,
                                    winner2 = winner2,
                                    winner3 = winner3,
                                    isFavourite = 0
                                )
                            )

                            sportsName.add(EventsData(event = name, isFav = 0))

                        }

                        DocumentChange.Type.MODIFIED -> {

                            match_no = try {
                                dc.document.id.toInt()
                            } catch (e: Exception) {
                                0
                            }
                            name = try {
                                dc.document["sport"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            round = try {
                                dc.document["round_name"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            round_type = try {
                                dc.document["round_type"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            team_1 = try {
                                dc.document["team1"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            team_2 = try {
                                dc.document["team2"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            time_stamp = try {
                                dc.document["timestamp"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            venue = try {
                                dc.document["venue"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            //TODO Check default Value
                            gender = try {
                                dc.document["gender"].toString()
                            } catch (e: Exception) {
                                "Boys"
                            }
                            isScore = try {
                                dc.document["is_score"] as Boolean
                            } catch (e: Exception) {
                                false
                            }
                            layout = try {
                                (dc.document["layout"] as Long).toInt()
                            } catch (e: Exception) {
                                2
                            }
                            score_1 = try {
                                dc.document["score1"].toString()
                            } catch (e: Exception) {
                                Log.e("sports1", "Exception = ${e.toString()}")
                                "Not Available"
                            }
                            score_2 = try {
                                dc.document["score2"].toString()
                            } catch (e: Exception) {
                                Log.e("sports1", "Exception = ${e.toString()}")
                                "Not Available"
                            }
                            winner1 = try {
                                dc.document["winner1"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            winner2 = try {
                                dc.document["winner2"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }
                            winner3 = try {
                                dc.document["winner3"].toString()
                            } catch (e: Exception) {
                                "Not Available"
                            }

                            sportsName.add(EventsData(event = name, isFav = 0))

                            eventsDao.updateSportsData(
                                matchNo = match_no,
                                name = name,
                                round = round,
                                roundType = round_type,
                                team1 = team_1,
                                team2 = team_2,
                                time = time_stamp,
                                venue = venue,
                                gender = gender,
                                isScore = isScore,
                                layout = layout,
                                score1 = score_1,
                                score2 = score_2,
                                winner1 = winner1,
                                winner2 = winner2,
                                winner3 = winner3)
                                .subscribeOn(Schedulers.io())
                                .subscribe({},{
                                    Log.e("EventsRepo", it.message, it)
                                })
                            Log.d("sports3", "Modified city: ${dc.document.data}")
                        }

                        DocumentChange.Type.REMOVED -> {
                            Log.d("sports4", "Removed city: ${dc.document.data}")
                            eventsDao.deleteSportsData(dc.document.id.toInt()).subscribeOn(Schedulers.io())
                        }
                    }
                }

                eventsDao.setSportName(sportsName).subscribeOn(Schedulers.io())
                    .subscribe({},{
                        Log.e("EventsRepo", it.message, it)
                })

                saveSportsDataRoom(sportsData).subscribe()
                Log.d("sports2", "added sports data a: $sportsData")
            }
    }

    @SuppressLint("CheckResult")
    private fun saveSportsDataRoom(sportsData: List<SportsData>): Completable {
        Log.d("sports2", "added sports data a: $sportsData")
        return eventsDao.saveSportsData(sportsData).subscribeOn(Schedulers.io())
            .doOnComplete {
                Log.d("Sports", "Data Saved")
            }
            .doOnError {
                Log.d("Sports", "Data Not Saved$it")
            }
    }

    fun updateMiscFavourite(eventId: String, favouriteMark: Int): Completable {
        return eventsDao.updateMiscFavourite(id = eventId, mark = favouriteMark)
            .subscribeOn(Schedulers.io())
    }

    fun updateSportsFavourite(matchNo: Int, favouriteMark: Int): Completable{
        return eventsDao.updateSportsFavourite(matchNo, favouriteMark)
            .subscribeOn(Schedulers.io())
    }

    fun updateEventFavourite(sport: String, favouriteMark: Int): Completable{
        return eventsDao.updateSportFav(sport, favouriteMark)
            .subscribeOn(Schedulers.io())
    }

    fun getEventEpc(event: String, eventId: String): Single<Pair<String, String>>{
        return eventsService.getEpcArticle(event, eventId).subscribeOn(Schedulers.io())
            .flatMap {response ->

                var pair: Pair<String, String>
                when(response.code()){

                    200 -> {
                         pair = Pair(response.body()!!.summary, response.body()!!.link)
                    }
                    else -> {
                        throw Exception("No description available")
                    }
                }

                return@flatMap Single.just(pair)
            }
    }
}





