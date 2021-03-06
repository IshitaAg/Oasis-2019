package com.dvm.appd.oasis.dbg.profile.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.User
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.profile.views.fragments.ProfileFragment
import com.dvm.appd.oasis.dbg.profile.views.fragments.UiState
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.PaytmRoom
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.UserShows
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


@SuppressLint("CheckResult")
class ProfileViewModel(val authRepository: AuthRepository,val walletRepository: WalletRepository) :ViewModel() {

    var order: LiveData<UiState> = MutableLiveData()
    var user:LiveData<User> = MutableLiveData()
    var balance:LiveData<String> = MutableLiveData()
    var userTickets: LiveData<List<UserShows>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)
    var tokens:LiveData<String> = MutableLiveData()

    init {

        walletRepository.moneyTracker.addUserListener()
        walletRepository.addTicketListener()

          authRepository.getUser().subscribe({
              (user as MutableLiveData).postValue(it!!)
              (error as MutableLiveData).postValue(null)
          },{
              Log.d("check",it.toString())
              if (it is UnknownHostException || it is SocketTimeoutException)
                  (error as MutableLiveData).postValue("Poor Internet Connection")
              else
                  (error as MutableLiveData).postValue(it.message)
          })

        walletRepository.getBalance().subscribe({
            (balance as MutableLiveData).postValue(it.toString())
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })

        walletRepository.getTockens().subscribe({
            tokens.asMut().postValue(it.toString())
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })

        walletRepository.getTicketInfo().subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })

        walletRepository.getAllUserShows()
            .subscribe({
                (userTickets as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                if (it is UnknownHostException || it is SocketTimeoutException)
                    (error as MutableLiveData).postValue("Poor Internet Connection")
                else
                    (error as MutableLiveData).postValue(it.message)
            })

    }

    fun logout(){
        (order as MutableLiveData).postValue(UiState.ShowLoading)
        authRepository.setUser(null).subscribe({
            walletRepository.disposeListener()
            walletRepository.moneyTracker.disposeListener()
            (order as MutableLiveData).postValue(UiState.MoveToLogin)
            (error as MutableLiveData).postValue(null)
        },{
            Log.d("checkl",it.toString())
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun refreshUserShows(){
        walletRepository.updateUserTickets().subscribe({
            (order as MutableLiveData).postValue(UiState.ShowIdle)
            (error as MutableLiveData).postValue(null)
        },{
            (order as MutableLiveData).postValue(UiState.ShowIdle)
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun refreshTicketsData(){
        walletRepository.getTicketInfo().subscribe({
            (order as MutableLiveData).postValue(UiState.ShowIdle)
            (error as MutableLiveData).postValue(null)
        },{
            (order as MutableLiveData).postValue(UiState.ShowIdle)
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun onPaytmTransactionSucessful(body: JsonObject, transaction: PaytmRoom): Single<Response<Void>> {
        Log.d("PayTm", "Entered on Sucess in View Model")
        return walletRepository.sendTransactionDetails(body, transaction).observeOn(AndroidSchedulers.mainThread())
    }
}