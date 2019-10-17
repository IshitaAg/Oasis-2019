package com.dvm.appd.oasis.dbg.events.data.room

import androidx.room.*
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EventsDao {

    @Query("SELECT * FROM fav_data")
    fun getAllFavourites(): Single<List<FavEvents>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEvents(events: List<EventData>)

    @Query("DELETE FROM events_data")
    fun deleteAllEvents()

    @Transaction
    fun deleteAndInsertEvents(events: List<EventData>){
        deleteAllEvents()
        insertAllEvents(events)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<CategoryData>)

    @Query("DELETE FROM event_categories")
    fun deleteCategories()

    @Transaction
    fun deleteAndInsertCategories(categories: List<CategoryData>){
        deleteCategories()
        insertCategories(categories)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavEvent(event: FavEvents): Completable

    @Query("DELETE FROM fav_data WHERE event_id = :eventId")
    fun deleteFavEvent(eventId: Int): Completable

    @Query("SELECT events_data.event_id AS eventId, events_data.name AS name, events_data.about AS about, events_data.rules AS rules, events_data.time AS time, events_data.date AS date, events_data.details AS details, events_data.venue AS venue,  COALESCE(fav_data.is_fav, 0) AS isFav FROM events_data LEFT JOIN fav_data ON events_data.event_id = fav_data.event_id WHERE events_data.date = :date ORDER BY events_data.event_id, events_data.time")
    fun getAllEventsByDate(date: String): Flowable<List<ModifiedEventsData>>

    @Query("SELECT events_data.event_id AS eventId, events_data.name AS name, events_data.about AS about, events_data.rules AS rules, events_data.time AS time, events_data.date AS date, events_data.details AS details, events_data.venue AS venue,  COALESCE(fav_data.is_fav, 0) AS isFav FROM events_data LEFT JOIN event_categories ON events_data.event_id = event_categories.event_id LEFT JOIN fav_data ON events_data.event_id = fav_data.event_id WHERE events_data.date = :date AND event_categories.filtered = 1 ORDER BY events_data.event_id, events_data.time")
    fun getEventsByCategory(date: String): Flowable<List<ModifiedEventsData>>

    @Query("SELECT DISTINCT date FROM events_data ORDER BY date")
    fun getEventsDates(): Flowable<List<String>>

    @Query("SELECT DISTINCT category, filtered FROM event_categories")
    fun getAllCategories(): Flowable<List<FilterData>>

    @Query("UPDATE event_categories SET filtered = :filtered WHERE category = :category")
    fun updateFiltered(category: String, filtered: Boolean): Completable

    @Query("UPDATE event_categories SET filtered = 0")
    fun removeFilters(): Completable
}