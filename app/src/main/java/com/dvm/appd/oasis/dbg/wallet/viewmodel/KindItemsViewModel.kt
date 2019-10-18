package com.dvm.appd.oasis.dbg.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.KindItems
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class KindItemsViewModel(val walletRepository: WalletRepository):ViewModel() {

    var toast:LiveData<String> = MutableLiveData()
    var items:LiveData<List<KindItems>> = MutableLiveData()
    init {
        walletRepository.fetchKindItems().subscribe({
            walletRepository.getKindItems().subscribe{
                Log.d("checkkindvm",it.toString())
                items.asMut().postValue(it)
            }
        },{
            if(it is SocketTimeoutException|| it is UnknownHostException)
            {
                toast.asMut().postValue("No internet Connection Found")
            }
            else
                toast.asMut().postValue(it.toString())
        })
    }

}