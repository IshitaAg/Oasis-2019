package com.dvm.appd.oasis.dbg.elas.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.dvm.appd.oasis.dbg.elas.model.UIStateElas
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import com.dvm.appd.oasis.dbg.elas.model.retrofit.PlayerRankingResponse
import com.dvm.appd.oasis.dbg.shared.util.asMut
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ElasViewModel(val elasRepository: ElasRepository) : ViewModel() {
    private val TAG = "ELAS REPO"

    var questions: LiveData<Map<Long, List<CombinedQuestionOptionDataClass>>> = MutableLiveData()
    var activeQuestion: LiveData<List<CombinedQuestionOptionDataClass>> = MutableLiveData()
    var uiState: LiveData<UIStateElas> =  MutableLiveData()
    var compositeDisposable = CompositeDisposable()
    var rulesForCurrentRound: LiveData<List<String>> = MutableLiveData()
    var leaderboard: LiveData<List<PlayerRankingResponse>> = MutableLiveData()

    init {
        uiState.asMut().postValue(UIStateElas.Loading)
        getQuestions()
        getLeaderboard()
        elasRepository.getQuestions().subscribe({
            Log.d("Elas Repo", "REterived data from room")
        },{
            (uiState as MutableLiveData).value = UIStateElas.Failure(it.message.toString())
        })
    }

    @SuppressLint("CheckResult")
    private fun getLeaderboard() {
        val d2 = elasRepository.getLeaderboardFromRoom().subscribeOn(Schedulers.io()).subscribe({
            Log.d("Elas ViewModel", "Observer for leaderboard entered with = ${it.toString()}")
            leaderboard.asMut().postValue(it)
        },{
            Log.e("ELASQViewModel", "Error in reading Leaderboard from room = ${it.message.toString()}")
            uiState.asMut().postValue(UIStateElas.Failure("Failed to recive data from server. Try Again"))
        })
        compositeDisposable.add(d2)
    }

    private fun getQuestions() {
        val d1 = elasRepository.getQuestionsFromRoom().subscribeOn(Schedulers.io()).doOnNext{
            Log.d(TAG, "Entered Observer with list = ${it.toString()}")
            uiState.asMut().postValue(UIStateElas.Questions(it.groupBy { it.questionId }))
            (questions as MutableLiveData<Map<Long, List<CombinedQuestionOptionDataClass>>>).postValue(it.groupBy { it.questionId })
        }.doOnError{
            Log.e(TAG, "Error in reading from room = ${it.message.toString()}")
            uiState.asMut().postValue(UIStateElas.Failure("Failed to recive data from server. Try Again"))
            reinitializeSubscription()
        }.subscribe()
        compositeDisposable.add(d1)
    }

    private fun reinitializeSubscription() {
        compositeDisposable.dispose()
        getQuestions()
        getLeaderboard()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}