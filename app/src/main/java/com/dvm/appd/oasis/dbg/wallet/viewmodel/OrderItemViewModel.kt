package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedOrdersData
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class OrderItemViewModel(val walletRepository: WalletRepository, val orderId: Int): ViewModel() {

    val order: LiveData<ModifiedOrdersData> = MutableLiveData()
    val progressBarMark: LiveData<Int> = MutableLiveData(1)
    var error: LiveData<String> = MutableLiveData(null)

    init {

        walletRepository.getOrderById(orderId)
            .subscribe({
                Log.d("OrderDetailVM", it.toString())
                (order as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                if (it is UnknownHostException || it is SocketTimeoutException)
                    (error as MutableLiveData).postValue("Poor Internet Connection")
                else
                    (error as MutableLiveData).postValue(it.message)
            })

    }

    fun rateOrder(shell: Int, rating: Int) {
        walletRepository.rateOrder(orderId, shell, rating)
            .subscribe({
                (progressBarMark as MutableLiveData).postValue(1)
                (error as MutableLiveData).postValue(null)
            },{
                (progressBarMark as MutableLiveData).postValue(1)
                if (it is UnknownHostException || it is SocketTimeoutException)
                    (error as MutableLiveData).postValue("Poor Internet Connection")
                else
                    (error as MutableLiveData).postValue(it.message)
            })
    }

    fun updateOtpSeen(orderId: Int){
        walletRepository.updateOtpSeen(orderId).subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

}