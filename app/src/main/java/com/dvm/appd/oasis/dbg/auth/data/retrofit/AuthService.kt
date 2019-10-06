package com.dvm.appd.oasis.dbg.auth.data.retrofit

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

  @POST("/wallet/auth")
  fun login(@Body body:JsonObject):Single<Response<AuthPojo>>
}