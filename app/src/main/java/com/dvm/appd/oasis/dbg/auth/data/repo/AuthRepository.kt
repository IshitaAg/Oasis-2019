package com.dvm.appd.oasis.dbg.auth.data.repo

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import com.dvm.appd.oasis.dbg.auth.data.User
import com.dvm.appd.oasis.dbg.auth.data.retrofit.AuthService
import com.dvm.appd.oasis.dbg.auth.views.LoginState
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.security.Key

class AuthRepository(val authService: AuthService, val sharedPreferences: SharedPreferences) {

    object Keys {

        const val name = "NAME"
        const val email = "EMAIL"
        const val contact = "CONTACT"
        const val jwt = "JWT"
        const val userId = "ID"
        const val qrCode = "QR"
        const val isBitsian = "ISBITSIAN"
        const val REGTOKEN = "REGTOKEN"
        const val TOPIC_SUBSCRIPTION = "TOPIC_SUBSCRIPTION"
        const val first_login = "FIRST_LOGIN"
        const val voted="VOTED"
        const val referralCode = "REFERRAL_CODE"
        const val referredBy = "REFERRED_BY"
    }

    fun loginOutstee(username: String, password: String): Single<LoginState> {
        val regToken = sharedPreferences.getString(Keys.REGTOKEN, "")
        val referralCode = sharedPreferences.getString(Keys.referralCode, "")
        val body = JsonObject().also {
            it.addProperty("username", username)
            it.addProperty("password", password)
            if(regToken != "")
                it.addProperty("reg_token", regToken)
            if(referralCode != "")
                it.addProperty("referral_code", referralCode)
        }

        Log.d("check", body.toString())
        return login(body, false)
    }

    fun loginBitsian(id:String):Single<LoginState>{
        val regToken = sharedPreferences.getString(Keys.REGTOKEN, "Default Value")
        val referralCode = sharedPreferences.getString(Keys.referralCode, "")
        val body = JsonObject().also {
            it.addProperty("id_token",id)
            if(regToken != "")
                it.addProperty("reg_token", regToken)
            if(referralCode != "")
                it.addProperty("referral_code", referralCode)
        }
        Log.d("check",body.toString())
        return login(body,true)
    }

    fun getUser(): Maybe<User> {
        val name = sharedPreferences.getString(Keys.name, null)
        val email = sharedPreferences.getString(Keys.email, null)
        val contact = sharedPreferences.getString(Keys.contact, null)
        val jwt = sharedPreferences.getString(Keys.jwt, null)
        val id = sharedPreferences.getString(Keys.userId, null)
        val qr = sharedPreferences.getString(Keys.qrCode, null)
        val bitsian = sharedPreferences.getBoolean(Keys.isBitsian, false)
        val firstLogin = sharedPreferences.getBoolean(Keys.first_login,false)
        val voted = sharedPreferences.getBoolean(Keys.voted,false)
        val referralCode = sharedPreferences.getString(Keys.referralCode, "")
        Log.d("checkSp", listOf(name, email, contact, jwt, qr, bitsian,firstLogin).toString())
        if (listOf(name, email, contact, jwt, qr).contains(null)) {
            setUser(null).subscribe()
            return Maybe.empty()
        }
        return Maybe.just(User(jwt!!, name!!, id!!, email!!, contact!!, qr!!,bitsian,firstLogin,voted,referralCode!!))
    }

    @SuppressLint("ApplySharedPref")
    fun setUser(user: User?): Completable {
        return Completable.fromAction {
            sharedPreferences.edit().apply {
                putString(Keys.jwt, user?.jwt)
                putString(Keys.name, user?.name)
                putString(Keys.userId, user?.userId)
                putString(Keys.email, user?.email)
                putString(Keys.contact, user?.phone)
                putString(Keys.qrCode, user?.qrCode)
                putBoolean(Keys.isBitsian, user?.isBitsian?:false)
                putBoolean(Keys.first_login,user?.firstLogin?:false)
                putBoolean(Keys.voted,user?.voted?:false)
                putString(Keys.referralCode,user?.referralCode?:"")
            }.commit()
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun addRegToken(token: String) {
        Log.d("Auth Repo", "Entered to add token")
        sharedPreferences.edit().putString(Keys.REGTOKEN, token).apply()
    }

    fun addReferral(code: String) {
        Log.d("Auth Repo", "Recived Referral Code = $code")
        sharedPreferences.edit().putString(Keys.referredBy, code).apply()
    }

    fun login(body: JsonObject, bitsian: Boolean): Single<LoginState> {
        return authService.login(body)
            .flatMap { response ->
                Log.d("checkr", response.code().toString())
                Log.d("checkr", response.body().toString())


                when (response.code()) {
                    200 -> {
                        Log.d("checkr", response.body().toString())
                        var name = response.body()!!.name
                        var firstLogin = sharedPreferences.getBoolean(Keys.first_login, true)
                        setUser(
                            User(
                                jwt = response.body()!!.jwt,
                                name = response.body()!!.name,
                                userId = response.body()!!.userId,
                                email = response.body()!!.email,
                                phone = response.body()!!.phone,
                                qrCode = response.body()!!.qrCode,
                                isBitsian = bitsian,
                                firstLogin = firstLogin,
                                voted = false,
                                referralCode = response.body()!!.referralCode
                            )
                        ).subscribe()
                        Single.just(LoginState.Success)
                    }
                    in 400..499 -> Single.just(LoginState.Failure(response.errorBody()!!.string()))
                    else -> Single.just(LoginState.Failure("Something went wrong!!"))
                }

            }.doOnError {
                Log.d("checkre", it.toString())
            }
            .subscribeOn(Schedulers.io())
    }

    fun subscribeToTopics() {
        if (!sharedPreferences.getBoolean(Keys.TOPIC_SUBSCRIPTION, false)) {
            FirebaseMessaging.getInstance().subscribeToTopic("User").addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.e("Auth Repo", "Falied to subscribe to topic")
                    return@addOnCompleteListener
                }
                val isBitsian = sharedPreferences.getBoolean(Keys.isBitsian, false)
                val topic = if (isBitsian) {
                    "Bitsian"
                } else {
                    "Outstee"
                }
                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.e("Auth Repo", "Falied to subscribe to topic $topic")
                        return@addOnCompleteListener
                    }
                    sharedPreferences.edit().putBoolean(Keys.TOPIC_SUBSCRIPTION, true).apply()
                }
            }
        }
    }

    fun disableOnBoardingForUser() {

        sharedPreferences.edit().putBoolean(Keys.first_login, false).apply()
    }
}