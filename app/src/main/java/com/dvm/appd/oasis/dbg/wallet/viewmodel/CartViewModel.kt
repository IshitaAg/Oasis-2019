package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData

class CartViewModel(val walletRepository: WalletRepository): ViewModel(){

    var cartItems: LiveData<List<ModifiedCartData>> = MutableLiveData()
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    var error: LiveData<String> = MutableLiveData(null)
    var redirect: LiveData<Boolean> = MutableLiveData(false)

    init {

        walletRepository.getAllModifiedCartItems()
            .subscribe({
                Log.d("CartVM", it.toString())
                (cartItems as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun placeOrder(){
        walletRepository.placeOrder().subscribe({
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue("Order successful")
            (redirect as MutableLiveData).postValue(true)
        },{
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun deleteCartItem(itemId: Int){
        walletRepository.deleteCartItem(itemId).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun updateCartItems(itemId: Int, quantity: Int){
        walletRepository.updateCartItems(itemId, quantity).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }
}