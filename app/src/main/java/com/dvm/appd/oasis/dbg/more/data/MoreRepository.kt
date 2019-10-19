package com.dvm.appd.oasis.dbg.more.data

import android.content.SharedPreferences
import android.util.Log
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.more.ComediansVoting
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import io.reactivex.Completable
import io.reactivex.Flowable

class MoreRepository(val sharedPreferences: SharedPreferences,val comediansVoting: ComediansVoting) {

    fun isVotingEnabled(): Flowable<Boolean> {
        return comediansVoting.getStatus()
    }

    fun getComedians(): Flowable<List<Comedian>> {
        return comediansVoting.getComedians()
    }

    fun voteForComedian(name:String): Completable {
        return comediansVoting.vote(name).doOnComplete {
            sharedPreferences.edit().putBoolean(AuthRepository.Keys.voted,true).apply()
        }.doOnError {
            Log.d("checke",it.toString())
        }
    }
}