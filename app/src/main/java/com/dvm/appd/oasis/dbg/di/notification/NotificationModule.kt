package com.dvm.appd.oasis.dbg.di.notification

import com.dvm.appd.oasis.dbg.notification.NotificationDao
import com.dvm.appd.oasis.dbg.notification.NotificationRepository
import com.dvm.appd.oasis.dbg.shared.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class NotificationModule {

    @Provides
    fun provideNotificationRepository(notificationDao: NotificationDao) : NotificationRepository {
        return NotificationRepository(notificationDao)
    }

    @Provides
    fun provideNotificationDao(appDatabase: AppDatabase) : NotificationDao{
        return appDatabase.notificationDao()
    }

}