package com.dvm.appd.oasis.dbg.elas.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.card_elas__question_fragment_options.view.*
import java.util.concurrent.TimeUnit

class ElasOptionsAdapter(val listener: OnOptionSelected): RecyclerView.Adapter<ElasOptionsAdapter.ElasOptionViewHolder>() {

    var optionsList: List<CombinedQuestionOptionDataClass> = emptyList()
    var optionSelected: Int = -1

    interface OnOptionSelected {
        fun optionSelected(position: Long)
        fun noOptionSelected()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElasOptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_elas__question_fragment_options, parent, false)
        return ElasOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    override fun onBindViewHolder(holder: ElasOptionViewHolder, position: Int) {
        if (optionSelected == position) {
            holder.parent.setBackgroundColor(Color.parseColor("#ECEBFF"))
            holder.checkImg.visibility = View.VISIBLE
        } else {
            holder.parent.setBackgroundColor(Color.WHITE)
            holder.checkImg.visibility = View.INVISIBLE
        }
        holder.optionNumber.text = "${(65+position).toChar()}.\t"
        holder.option.text = optionsList[position].option
        RxView.clicks(holder.parent).debounce(500, TimeUnit.MILLISECONDS).subscribe {
            Log.d("Elas Option Adapter", "Enetered OnClick Listener")
            if (optionSelected == position) {
                optionSelected = -1
                holder.parent.setBackgroundColor(Color.WHITE)
                holder.checkImg.visibility = View.INVISIBLE
                listener.noOptionSelected()
            } else {
                optionSelected = position
                holder.parent.setBackgroundColor(Color.parseColor("#ECEBFF"))
                holder.checkImg.visibility = View.VISIBLE
                listener.optionSelected(optionsList[position].option_id)
            }
        }
    }


    inner class ElasOptionViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val parent = view.parent_card_elasQuestionFrag_options
        val optionNumber = view.text_option_number
        val option = view.text_option_text
        val checkImg = view.img_isOptionSelected
    }
}