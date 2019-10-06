package com.dvm.appd.oasis.dbg.elas.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.elas.model.UIStateElas
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import com.dvm.appd.oasis.dbg.shared.util.asMut

class ElasQuestionViewModel(val repository: ElasRepository): ViewModel() {

    var question: LiveData<List<CombinedQuestionOptionDataClass>> = MutableLiveData()
    var uiState: LiveData<UIStateElas> = MutableLiveData()

    init {
        uiState.asMut().postValue(UIStateElas.Loading)
    }

    @SuppressLint("CheckResult")
    fun getQuestion(id: Long) {
        repository.getParticularQuestionFromRoom(id).subscribe({
            if (it != null) {
                question.asMut().postValue(it)
                uiState.asMut().postValue(UIStateElas.Questions(mapOf(Pair(it.first().questionId, it))))
            } else {
                uiState.asMut().postValue(UIStateElas.Failure("Question not found in local database"))
            }
        },{
            uiState.asMut().postValue(UIStateElas.Failure("Error in Local database. Please restart the app"))
            Log.e("QuestionViewModel", "Failed to get question from room = ${it.toString()}")
        })
    }

    @SuppressLint("CheckResult")
    fun submitAnswer(questionId: Long, optionId: Long) {
        uiState.asMut().postValue(UIStateElas.Loading)
        repository.submitAnswerForQuestion(questionId, optionId).subscribe({
            Log.d("ElasQuesViewModel", "Response Code = ${it.code()}")
            when(it.code()) {
                200 -> {
                    uiState.asMut().postValue(UIStateElas.Failure(it.body()!!.display_message))
                }
                400 -> {
                    Log.d("ElasQuesViewModel", "Response Body = ${it.body().toString()} ${it.body()}")
                    uiState.asMut().postValue(UIStateElas.Failure("You have already submitted the answer"))
                }
                else -> {
                    Log.d("ElasQuesViewModel", "Response Body = ${it.body().toString()} ${it.body()}")
                    uiState.asMut().postValue(UIStateElas.Failure("Unable to submit Answer(${it.code()} )"))
                }
            }

        },{
            uiState.asMut().postValue(UIStateElas.Failure("Couldn't Submit your answer at this point. Try again later"))
        })
    }

}