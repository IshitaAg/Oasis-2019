package com.dvm.appd.oasis.dbg.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.profile.views.fragments.TransactionResult
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SendMoneyViewModel(val walletRepository: WalletRepository):ViewModel() {
    var result: LiveData<TransactionResult> = MutableLiveData()
    init {

    }

    fun transferMoney(id:Int,amount:Int){
       walletRepository.transferMoney(id,amount).subscribe({
           (result as MutableLiveData).postValue(it)
       },{
           if (it is UnknownHostException || it is SocketTimeoutException)
               (result as MutableLiveData).postValue(TransactionResult.Failure("Poor Internet Connection"))
           else
           (result as MutableLiveData).postValue(TransactionResult.Failure(it.message.toString()))
       })
    }
}