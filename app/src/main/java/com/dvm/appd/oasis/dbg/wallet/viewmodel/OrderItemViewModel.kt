package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedOrdersData

class OrderItemViewModel(val walletRepository: WalletRepository, val orderId: Int): ViewModel() {

    val order: LiveData<ModifiedOrdersData> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)

    init {

        walletRepository.getOrderById(orderId)
            .subscribe({
                Log.d("OrderDetailVM", it.toString())
                (order as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun rateOrder(shell: Int, rating: Int) {
        walletRepository.rateOrder(orderId, shell, rating)
            .subscribe({
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })
    }

}