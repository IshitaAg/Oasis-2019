package com.dvm.appd.oasis.dbg.events.data.room

import androidx.room.*
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.EventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.SportsData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMiscEventData(events: List<MiscEventsData>): Completable

    @Query("DELETE FROM misc_table")
    fun deleteAllMiscEvents()

    @Transaction
    fun updateAllMiscEvents(events: List<MiscEventsData>){
        deleteAllMiscEvents()
        insertMiscEventData(events)
    }

    @Query("UPDATE misc_table SET favourite = :mark WHERE event_id = :id")
    fun updateMiscFavourite(id: String, mark: Int): Completable

    @Query("UPDATE misc_table SET event_name = :name, event_venue = :venue, event_time = :time, event_description = :description, event_day = :day, organiser = :organiser WHERE event_id = :id")
    fun updateMiscData(id: String, name: String, venue: String, time: String, description: String, day: String, organiser: String): Completable

    @Query("DELETE FROM misc_table WHERE event_id = :id")
    fun deleteMiscEvent(id: String): Completable

    @Query("SELECT * FROM misc_table WHERE event_day = :day OR event_day = 'Day 6' ORDER BY event_day, event_time")
    fun getDayMiscEvents(day: String): Flowable<List<MiscEventsData>>

    @Query("SELECT DISTINCT event_day FROM misc_table WHERE event_day != 'Day 6' ORDER BY event_day")
    fun getMiscDays(): Flowable<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setSportName(names: List<EventsData>): Completable

    @Query("SELECT * FROM events")
    fun getSportsName(): Flowable<List<EventsData>>

    @Query("UPDATE events SET is_fav = :favMark WHERE event = :sport")
    fun updateSportFav(sport: String, favMark: Int): Completable
    //Sports
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveSportsData(sportsData: List<SportsData>): Completable

    @Query("DELETE FROM sports_table WHERE match_no = :matchNo")
    fun deleteSportsData(matchNo: Int): Completable

    @Query("UPDATE sports_table SET layout = :layout, gender = :gender, sport_name = :name, venue = :venue, time = :time, round = :round, round_type = :roundType, team_1 = :team1, team_2 = :team2, is_score = :isScore, score_1 = :score1, score_2 = :score2, winner_1 = :winner1, winner_2 = :winner2, winner_3 = :winner3 WHERE match_no = :matchNo")
    fun updateSportsData(matchNo: Int, layout: Int, gender: String, name: String, venue: String, time: String, round: String, roundType: String, team1: String, team2: String, isScore: Boolean, score1: String, score2: String, winner1: String, winner2: String, winner3: String): Completable

    @Query("SELECT DISTINCT gender FROM sports_table  WHERE sport_name = :name ORDER BY gender")
    fun getDistinctGenderForSport(name: String): Flowable<List<String>>

    @Query("SELECT * FROM sports_table WHERE sport_name = :name ORDER BY time")
    fun getSportDataForSport(name: String): Flowable<List<SportsData>>

    @Query("UPDATE sports_table SET is_favourite = :favouriteMark WHERE match_no = :matchNo")
    fun updateSportsFavourite(matchNo: Int, favouriteMark: Int): Completable

    @Query("SELECT event FROM events WHERE is_fav = 1 UNION SELECT event_name FROM misc_table WHERE favourite = 1 UNION SELECT DISTINCT sport_name FROM sports_table WHERE is_favourite = 1")
    fun getAllFavourites(): Single<List<String>>
}