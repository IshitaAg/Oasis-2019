package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.adapter_comedians.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ComediansAdapter(val listener: onVoteBtnClicked) :
    RecyclerView.Adapter<ComediansAdapter.ComediansViewHolder>() {

    var comedians: List<Comedian> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComediansAdapter.ComediansViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_comedians, parent, false)
        return ComediansViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comedians.size
    }
   var lastChecked:RadioButton?=null
    override fun onBindViewHolder(holder: ComediansAdapter.ComediansViewHolder, position: Int) {
        holder.comName.text = comedians[position].name
        holder.voteBtn.isClickable=false
        RxView.clicks(holder.parent).debounce(200, TimeUnit.MILLISECONDS).observeOn(
            AndroidSchedulers.mainThread()).subscribe {
            holder.voteBtn.isChecked=true
            if(lastChecked!=null){
                lastChecked!!.isChecked = false
            }
            listener.voted(comedians[position].name)
            lastChecked = holder.voteBtn
        }
    }

    inner class ComediansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val voteBtn = view.checkBtn
        val comName = view.comedianName
        val parent = view.voteView

    }

    interface onVoteBtnClicked {
        fun voted(name: String)
    }
}