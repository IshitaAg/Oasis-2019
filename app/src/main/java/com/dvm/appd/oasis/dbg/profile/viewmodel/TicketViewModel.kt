package com.dvm.appd.oasis.dbg.profile.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedTicketsData
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.TicketsCart
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("CheckResult")
class TicketViewModel(val walletRepository: WalletRepository): ViewModel(){

    var tickets: LiveData<List<ModifiedTicketsData>> = MutableLiveData()
    var progressBarMark: LiveData<Int> = MutableLiveData(1)
    var error: LiveData<String> = MutableLiveData(null)
    var redirect: LiveData<Boolean> = MutableLiveData(false)

    init {

        walletRepository.getAllTicketData().subscribe({
            Log.d("TicketsShows", "$it")
            (tickets as MutableLiveData).postValue(it)
            (progressBarMark as MutableLiveData).postValue(1)
            (error as MutableLiveData).postValue(null)
        }, {
            (progressBarMark as MutableLiveData).postValue(1)
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })

    }

    fun buyTickets(){
        walletRepository.buyTickets().subscribe({
            Log.d("Wallet Repo", "Entered success")
            (progressBarMark as MutableLiveData).postValue(1)
            (redirect as MutableLiveData).postValue(true)
            (error as MutableLiveData).postValue("Tickets bought successfully")
        },{
            Log.d("Wallet Repo", "Errror in Api call = ${it}")
            (progressBarMark as MutableLiveData).postValue(1)
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun insertTicketCart(ticket: TicketsCart){
        walletRepository.insertTicketsCart(ticket).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun deleteTiceketCartItem(id: Int){
        walletRepository.deleteTicektsCartItem(id).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }

    fun updateTicketCart(quantity: Int, id: Int){
        walletRepository.updateTicketsCart(quantity, id).subscribe({
            (error as MutableLiveData).postValue(null)
        },{
            if (it is UnknownHostException || it is SocketTimeoutException)
                (error as MutableLiveData).postValue("Poor Internet Connection")
            else
                (error as MutableLiveData).postValue(it.message)
        })
    }
}