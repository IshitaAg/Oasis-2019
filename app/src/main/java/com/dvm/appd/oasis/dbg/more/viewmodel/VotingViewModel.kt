package com.dvm.appd.oasis.dbg.more.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.events.data.repo.EventsRepository
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.dvm.appd.oasis.dbg.shared.util.asMut

class VotingViewModel(val eventsRepository: EventsRepository,val authRepository: AuthRepository):ViewModel() {

    val comedians: LiveData<List<Comedian>> = MutableLiveData()
    var voteState:LiveData<String> = MutableLiveData()
    var toast:LiveData<String> = MutableLiveData()
    init {
        authRepository.getUser().subscribe({user->
            when(user.voted!!){
                true->{
                        voteState.asMut().postValue("Voted")
                }
                false ->{
                   eventsRepository.isVotingEnabled().subscribe {enabled ->
                       eventsRepository.getComedians().subscribe{comedianNames->
                           when(enabled!!){
                               true -> {
                                   comedians.asMut().postValue(comedianNames)
                                   voteState.asMut().postValue("Enabled")
                               }
                               false -> {
                                   comedians.asMut().postValue(listOf())
                                   voteState.asMut().postValue("Closed")
                               }
                           }
                       }
                   }
                }
            }
        },{
            toast.asMut().postValue(it.toString())
        })
    }

   fun vote(name:String){
       eventsRepository.voteForComedian(name).subscribe({

       },{
           toast.asMut().postValue(it.toString())
       })
   }
}