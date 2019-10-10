package com.dvm.appd.oasis.dbg.events.data.repo

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Single
import javax.inject.Inject

class EventsSyncWorker(appContext: Context, workerParams: WorkerParameters): RxWorker(appContext, workerParams){

    @Inject
    lateinit var eventsRepository: EventsRepository

    override fun createWork(): Single<Result> {

        Log.d("WorkManager", "It works")
        return eventsRepository.getEventsData()
    }

}