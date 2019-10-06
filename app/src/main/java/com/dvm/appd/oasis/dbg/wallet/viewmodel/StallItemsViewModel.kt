package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.CartData
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedStallItemsData

class StallItemsViewModel(val walletRepository: WalletRepository, val stallId: Int) : ViewModel() {

    var items:LiveData<List<Pair<String, List<ModifiedStallItemsData>>>> = MutableLiveData()
    var cartItems: LiveData<List<ModifiedCartData>> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData(null)

    init {


        walletRepository.getItemsForStall(stallId)
            .subscribe({
                Log.d("StallItemVM", it.toString())
                (items as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            }
                , {
                    Log.d("checkve", it.toString())
                    (error as MutableLiveData).postValue(it.message)
                }
            )

        walletRepository.getAllModifiedCartItems()
            .subscribe({
                Log.d("CartVM", it.toString())
                (cartItems as MutableLiveData).postValue(it)
                (error as MutableLiveData).postValue(null)
            },{
                (error as MutableLiveData).postValue(it.message)
            })

    }

    fun deleteCartItem(itemId: Int) {
        walletRepository.deleteCartItem(itemId).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }

    fun insertCartItems(cartData: CartData) {
        walletRepository.insertCartItems(cartData).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            (error as MutableLiveData).postValue(it.message)
        })
    }
}