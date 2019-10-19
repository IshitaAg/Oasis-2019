package com.dvm.appd.oasis.dbg.elas.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.card_recycler_elas_frag_questions.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ElasQuestionsAdapter(val listener: onQuestionButtonClicked) : RecyclerView.Adapter<ElasQuestionsAdapter.ElasQuestionsViewHolder>() {

    interface onQuestionButtonClicked {
        fun answerQuestion(questionId: Long)
        fun viewRules(questionId: String)
    }

    var questionsList: Map<Long, List<CombinedQuestionOptionDataClass>> = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElasQuestionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_recycler_elas_frag_questions, parent, false)
        return ElasQuestionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }

    override fun onBindViewHolder(holder: ElasQuestionsViewHolder, position: Int) {
        holder.textQuestionNumber.text = "Question ${questionsList.toList()[position].second.first().questionId}"
        holder.textQuestion.text = questionsList.toList()[position].second.first().question
        RxView.clicks(holder.buttonRules).debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.d("QuesAdapter", "Entered onClick Listener with ${questionsList.toList()[position].second.first().category}")
            listener.viewRules(questionsList.toList()[position].second.first().category)
        }
        RxView.clicks(holder.parent).debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            listener.answerQuestion(questionsList.toList()[position].second.first().questionId)
        }
    }

    inner class ElasQuestionsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textQuestionNumber = view.text_card_elasFrag_questionNo
        val textQuestion = view.text_card_elasFrag_question
        val buttonRules = view.text_card_elasFrag_Rules
        val parent = view.parent_card_elasFrag_questions
    }
}