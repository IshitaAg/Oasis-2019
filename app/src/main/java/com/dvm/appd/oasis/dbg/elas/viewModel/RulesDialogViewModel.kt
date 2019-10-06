package com.dvm.appd.oasis.dbg.elas.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import com.dvm.appd.oasis.dbg.shared.util.asMut
import java.lang.Exception

class RulesDialogViewModel(val elasRepository: ElasRepository): ViewModel() {

    var rules: LiveData<List<String>> = MutableLiveData()
    var isLoading: LiveData<Boolean> = MutableLiveData()
    var error: LiveData<String> = MutableLiveData()

    init {
        isLoading.asMut().postValue(true)
    }

    @SuppressLint("CheckResult")
    fun getRulesForRound(round: String) {
        elasRepository.getRulesForRoundsFromBackend().subscribe({
            Log.d("RulesViewModel", "Response Code = ${it.code()}")
            when(it.code()) {
                200 -> {
                    var body = it.body()
                    Log.d("ViewModelRule", "Recived body = ${it.body().toString()}")
                    try {
                        var rule = body!!.rules.find {
                            it.roundId == round
                        }
                        if (rule != null) {
                            Log.d("Elas ViewModel", "Enetered Rule Listener with data = ${rule.toString()}")
                            rules.asMut().postValue(rule.rules_text)
                        }
                    } catch (e: Exception) {
                        Log.e("ElasViewModel", "Error in parsing body received from backend = ${it.toString()}")
                    }
                }
                else -> {
                    error.asMut().postValue(it.body().toString())
                }
            }
        },{
            Log.e("ElasViewModel", "Error in reading rules from backend = ${it.toString()}")
            error.asMut().postValue("Failed to recive data from server. Try Again")
        })
    }

}