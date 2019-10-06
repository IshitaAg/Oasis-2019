package com.dvm.appd.oasis.dbg.wallet.data.retrofit

import com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses.AllOrdersPojo
import com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses.AllTicketsPojo
import com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses.AllUserShowsPojo
import com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses.StallsPojo
import com.dvm.appd.bosm.dbg.wallet.data.retrofit.dataclasses.ChecksumPojo
import com.dvm.appd.bosm.dbg.wallet.data.retrofit.dataclasses.PaytmPojo
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface WalletService {

    @GET("wallet/vendors")
    fun getAllStalls():Single<Response<List<StallsPojo>>>

    @GET("wallet/orders")
    fun getAllOrders(@Header("Authorization") jwt: String): Single<Response<List<AllOrdersPojo>>>

    @POST("wallet/orders")
    fun placeOrder(@Header("Authorization") jwt: String, @Body body: JsonObject): Single<Response<AllOrdersPojo>>

    @POST("wallet/orders/make_otp_seen")
    fun makeOtpSeen(@Header("Authorization") jwt: String, @Body body: JsonObject): Single<Response<Unit>>

    @POST("wallet/monetary/add/swd")
    fun addMoneyBitsian(@Header("Authorization")jwt:String, @Body body:JsonObject):Single<Response<Unit>>

    @POST("wallet/monetary/transfer")
    fun transferMoney(@Header("Authorization")jwt:String,@Body body: JsonObject):Single<Response<Void>>

    @POST("wallet/ratings/{shell}/{orderId}")
    fun rateOrder(@Header("Authorization")jwt:String,@Body body: JsonObject, @Path("orderId")orderId: Int, @Path("shell")shell: Int): Single<Response<Unit>>

    @GET("tickets-manager/shows")
    fun getAllTickets(@Header("Authorization")jwt:String): Single<Response<AllTicketsPojo>>

    @GET("tickets-manager/tickets")
    fun getUserTickets(@Header("Authorization")jwt:String): Single<Response<AllUserShowsPojo>>

    @POST("tickets-manager/signup")
    fun buyTickets(@Header("Authorization")jwt:String,@Body body: JsonObject): Single<Response<Void>>

    //Paytm
    @POST("/backendlink to generate checksum")
    fun getCheckSum(paytmPojo: PaytmPojo): Single<Response<ChecksumPojo>>
}