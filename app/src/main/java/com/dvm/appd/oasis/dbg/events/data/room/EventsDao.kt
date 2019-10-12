package com.dvm.appd.oasis.dbg.events.data.room

import androidx.room.*
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EventsDao {

    @Query("SELECT name FROM events_data WHERE event_id = 1 UNION SELECT event_name FROM misc_table WHERE favourite = 1")
    fun getAllFavourites(): Single<List<String>>

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
    fun insertVenues(venues: List<VenueData>)

    @Query("DELETE FROM event_venues")
    fun deleteVenues()

    @Transaction
    fun deleteAndInsertVenues(venues: List<VenueData>){
        deleteVenues()
        insertVenues(venues)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavEvent(event: FavEvents): Completable

    @Query("DELETE FROM fav_data WHERE event_id = :eventId")
    fun deleteFavEvent(eventId: Int): Completable

    @Query("SELECT events_data.event_id AS eventId, events_data.name AS name, events_data.about AS about, events_data.rules AS rules, events_data.time AS time, events_data.date_time AS dateTime, events_data.duration AS duration, events_data.image_url AS imageUrl, events_data.details AS details, event_venues.venue AS venue, event_categories.category AS category, COALESCE(fav_data.is_fav, 0) AS isFav FROM events_data LEFT JOIN event_venues ON events_data.event_id = event_venues.event_id LEFT JOIN event_categories ON events_data.event_id = event_categories.event_id LEFT JOIN fav_data ON events_data.event_id = fav_data.event_id ORDER BY events_data.event_id")
    fun getAllEvents(): Flowable<List<ChildEventsData>>
}