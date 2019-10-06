package com.dvm.appd.oasis.dbg.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.profile.views.fragments.TransactionResult
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository

class AddMoneyViewModel(val walletRepository: WalletRepository):ViewModel() {

    var result:LiveData<TransactionResult> = MutableLiveData()
    init{

    }
    fun addMoney(amount:Int){
        walletRepository.addMoneyBitsian(amount).subscribe({
            (result as MutableLiveData).postValue(it)
        },{
            (result as MutableLiveData).postValue(TransactionResult.Failure(it.message.toString()))
        })

    }
}