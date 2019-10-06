package com.dvm.appd.oasis.dbg.profile.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.User
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.profile.views.fragments.UiState
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.UserShows

@SuppressLint("CheckResult")
class ProfileViewModel(val authRepository: AuthRepository,val walletRepository: WalletRepository) :ViewModel() {

    var order: LiveData<UiState> = MutableLiveData()
    var user:LiveData<User> = MutableLiveData()
    var balance:LiveData<String> = MutableLiveData()
    var userTickets: LiveData<List<UserShows>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)

    init {

        walletRepository.moneyTracker.addUserListener()
        walletRepository.addTicketListener()

          authRepository.getUser().subscribe({
              (user as MutableLiveData).postValue(it!!)
              (error as MutableLiveData).postValue(null)
          },{
              Log.d("check",it.toString())
              (error as MutableLiveData).postValue(it.message)
          })

        walletRepository.getBalance().subscribe({
            (balance as MutableLiveData).postValue(it.toString())
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })

        walletRepository.getTicketInfo().subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })

        walletRepository.getAllUserShows()
            .subscribe({
                (userTickets as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
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
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun refreshUserShows(){
        walletRepository.updateUserTickets().subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun refreshTicketsData(){
        walletRepository.getTicketInfo().subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }
}