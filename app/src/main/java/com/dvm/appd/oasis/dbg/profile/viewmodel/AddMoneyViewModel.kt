package com.dvm.appd.oasis.dbg.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.profile.views.fragments.TransactionResult
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository

class AddMoneyViewModel(val authRepository: AuthRepository,val walletRepository: WalletRepository):ViewModel() {

    var result:LiveData<TransactionResult> = MutableLiveData()
    var user:LiveData<Boolean> = MutableLiveData()
    init{
     authRepository.getUser().subscribe({
         user.asMut().postValue(it.isBitsian)
     },{
         Log.d("checke",it.toString())
     })
    }
    fun addMoney(amount:Int){
        walletRepository.addMoneyBitsian(amount).subscribe({
            (result as MutableLiveData).postValue(it)
        },{
            (result as MutableLiveData).postValue(TransactionResult.Failure(it.message.toString()))
        })

    }
}