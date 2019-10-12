package com.dvm.appd.oasis.dbg.wallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository

class KindItemsViewModel(val walletRepository: WalletRepository):ViewModel() {

    var toast:LiveData<String> = MutableLiveData()
    init {
       fetchFreshData()


    }

    fun fetchFreshData(){
        walletRepository.fetchKindItems().subscribe({

        },{
            toast.asMut().postValue(it.toString())
        })
    }
}