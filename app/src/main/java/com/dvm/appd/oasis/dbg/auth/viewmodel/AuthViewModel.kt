package com.dvm.appd.oasis.dbg.auth.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.views.LoginState
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.google.firebase.iid.FirebaseInstanceId
import java.lang.Exception

@SuppressLint("CheckResult")
class AuthViewModel(val authRepository: AuthRepository):ViewModel() {

    var state: LiveData<LoginState> = MutableLiveData()
//    var referral: LiveData<String> = MutableLiveData()
    var referralState: LiveData<Boolean> = MutableLiveData()

    init {
        (referralState as MutableLiveData).value = true
        listenRegToken()
    }

    private fun listenRegToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Auth ViewModel", "Errror Retriving Reg Token")
                return@addOnCompleteListener
            }
            Log.d("Auth ViewModel", "Reg Token recived = ${it.result!!.token}")
            authRepository.addRegToken(it.result!!.token)
        }.addOnFailureListener {
            listenRegToken()
            Log.e("Auth ViewModel", "Exception in listening for token \n ${it.toString()}")
        }
    }

    @SuppressLint("CheckResult")
    fun Blogin(id: String, code: String) {
        authRepository.loginBitsian(id, code).subscribe({
            authRepository.subscribeToTopics()
            when (it!!) {
                LoginState.Success -> {

                    authRepository.getUser().subscribe {
                        Log.d("checklogin",it.firstLogin.toString())
                        if (it.firstLogin) {
                            (state as MutableLiveData).postValue(LoginState.MoveToOnBoarding)
                            authRepository.disableOnBoardingForUser()
                        } else
                            (state as MutableLiveData).postValue(LoginState.MoveToMainApp)
                    }
                }
                is LoginState.Failure -> {
                    (state as MutableLiveData).postValue(it)
                }
            }
        }, {
            Log.d("checke", it.toString())
            (state as MutableLiveData).postValue(LoginState.Failure(it.message.toString()))
        })
    }

    fun login(username: String, password: String, code: String) {

        authRepository.loginOutstee(username, password, code).doOnSuccess {
            authRepository.subscribeToTopics()
            when(it!!){
                LoginState.Success -> {
                    authRepository.getUser().subscribe {
                        Log.d("checklogin",it.firstLogin.toString())
                        if(it.firstLogin==true) {
                            (state as MutableLiveData).postValue(LoginState.MoveToOnBoarding)
                            authRepository.disableOnBoardingForUser()
                        }

                        else {
                            // throw Exception("Random Error Occoures")
                            (state as MutableLiveData).postValue(LoginState.MoveToMainApp)

                        }
                    }
                }
                is LoginState.Failure -> {(state as MutableLiveData).postValue(it)}
            }

        }.doOnError {
            Log.d("checkve", it.toString())
            (state as MutableLiveData).postValue(LoginState.Failure(it.message.toString()))
        }.subscribe()

    }

    fun setReferral(referral: String){
        authRepository.addReferral(referral)
    }
//    authRepository.getUser().subscribe({
//        if (it.firstLogin){
//            refqeralState.asMut().postValue(true)
//        }
//        else{
//            referalState.asMut().postValue(false)
//        }
//    },{
//        (state as MutableLiveData).postValue(LoginState.Failure(it.message.toString()))
//    })
}