package com.dvm.appd.oasis.dbg.auth.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.views.LoginState
import com.google.firebase.iid.FirebaseInstanceId

class AuthViewModel(val authRepository: AuthRepository):ViewModel() {

    var state: LiveData<LoginState> = MutableLiveData()

    init {
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
    fun Blogin(id: String) {
        authRepository.loginBitsian(id).subscribe({
            authRepository.subscribeToTopics()
            when (it!!) {
                LoginState.Success -> {
                    authRepository.getUser().subscribe {
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

    fun login(username: String, password: String) {

        authRepository.loginOutstee(username, password).doOnSuccess {
            authRepository.subscribeToTopics()
            when(it!!){
                LoginState.Success -> {
                    authRepository.getUser().subscribe {
                        if(it.firstLogin==true) {
                            authRepository.disableOnBoardingForUser()
                            (state as MutableLiveData).postValue(LoginState.MoveToOnBoarding)
                        }
                        else
                            (state as MutableLiveData).postValue(LoginState.MoveToMainApp)
                    }
                }
                is LoginState.Failure -> {(state as MutableLiveData).postValue(it)}
            }

        }.doOnError {
            Log.d("checkve", it.toString())
        }.subscribe()

    }
}