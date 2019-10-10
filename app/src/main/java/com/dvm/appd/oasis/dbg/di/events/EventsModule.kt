package com.dvm.appd.oasis.dbg.di.events

import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.events.data.room.EventsDao
import com.dvm.appd.oasis.dbg.events.data.retrofit.EventsService
import com.dvm.appd.oasis.dbg.shared.AppDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EventsModule {

//    @Provides
//    fun provideEventsRepository(eventsDao: EventsDao, eventsService: EventsService): EventsRepository {
//        return EventsRepository(eventsDao = eventsDao, eventsService = eventsService)
//    }
//
//    @Provides
//    fun provideEventsDao(appDatabase: AppDatabase): EventsDao{
//        return appDatabase.eventsDao()
//    }
//
//    @Provides
//    fun provideEventsService(retrofit: Retrofit): EventsService{
//        return retrofit.create(EventsService::class.java)
//    }
}