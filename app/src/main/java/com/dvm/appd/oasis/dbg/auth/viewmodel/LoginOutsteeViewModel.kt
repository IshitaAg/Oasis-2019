package com.dvm.appd.oasis.dbg.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.views.LoginState

class LoginOutsteeViewModel(val authRepository: AuthRepository) : ViewModel() {

    val state: LiveData<LoginState> = MutableLiveData()

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