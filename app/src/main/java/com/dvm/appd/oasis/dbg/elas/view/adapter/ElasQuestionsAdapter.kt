package com.dvm.appd.oasis.dbg.elas.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import kotlinx.android.synthetic.main.card_recycler_elas_frag_questions.view.*

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
        holder.buttonRules.setOnClickListener {
            // Toast.makeText(holder.itemView.context!!,"To be announced",Toast.LENGTH_SHORT).show()
            Log.d("QuesAdapter", "Entered onClick Listener with ${questionsList.toList()[position].second.first().category}")
            listener.viewRules(questionsList.toList()[position].second.first().category)
        }
        holder.parent.setOnClickListener {
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