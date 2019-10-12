package com.dvm.appd.oasis.dbg.wallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.KindItems

class KindItemsViewModel(val walletRepository: WalletRepository):ViewModel() {

    var toast:LiveData<String> = MutableLiveData()
    var items:LiveData<List<KindItems>> = MutableLiveData()
    init {
        walletRepository.fetchKindItems().subscribe({
            walletRepository.getKindItems().subscribe{
                items.asMut().postValue(it)
            }
        },{
            toast.asMut().postValue(it.toString())
        })

    }

}