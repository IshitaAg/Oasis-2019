package com.dvm.appd.oasis.dbg.events.data.retrofit

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventsService {

    @GET("registrations/events")
    fun getAllEvents(): Single<Response<AllEventsPojo>>
}