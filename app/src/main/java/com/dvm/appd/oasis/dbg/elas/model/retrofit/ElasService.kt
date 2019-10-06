package com.dvm.appd.oasis.dbg.elas.model.retrofit

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ElasService {

    @POST("/quiz/answer")
    fun answerQuestion(@Header("Authorization") jwt: String, @Body body: JsonObject): Single<Response<AnswerResponse>>

    @GET("/quiz/get-user-details")
    fun getUserLeaderboardPosition(@Header("Authorization") jwt: String): Single<Response<PlayerRankingResponse>>

    @GET("/quiz/get-rules")
    fun getAllRules(): Single<Response<RulesResponse>>

    @GET("/quiz/get-previous-questions")
    fun getAllQuestions(@Header("Authorization")jwt: String):Single<Response<QuestionsResponse>>

}