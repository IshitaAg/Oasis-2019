package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.StallData
import com.dvm.appd.oasis.dbg.wallet.views.StallResult
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class StallsViewModel(val walletRepository: WalletRepository):ViewModel() {

    var stalls:LiveData<List<StallData>> = MutableLiveData()
    var result: LiveData<StallResult> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)

  init{
      (result as MutableLiveData).postValue(StallResult.Failure)

      refreshData()

      walletRepository.getAllStalls().subscribe({
          Log.d("check", it.toString())
          (stalls as MutableLiveData).postValue(it)
          (result as MutableLiveData).postValue(StallResult.Success)
      },{
          Log.d("check", it.message)
          (error as MutableLiveData).postValue(it.message)
      })


  }

    fun refreshData() {
        walletRepository.fetchAllStalls().subscribe({
            (result as MutableLiveData).postValue(StallResult.Success)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)

            (result as MutableLiveData).postValue(StallResult.Success)
        })
    }
}