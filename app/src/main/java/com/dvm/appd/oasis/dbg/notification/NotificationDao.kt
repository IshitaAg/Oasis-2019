package com.dvm.appd.oasis.dbg.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: Notification): Completable

    @Query("SELECT * FROM notification_table ORDER BY id DESC")
    fun getLoggedNotifications(): Flowable<List<Notification>>

}