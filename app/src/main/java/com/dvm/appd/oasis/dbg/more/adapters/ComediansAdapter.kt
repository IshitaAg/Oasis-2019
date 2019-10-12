package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import kotlinx.android.synthetic.main.adapter_comedians.view.*

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

    override fun onBindViewHolder(holder: ComediansAdapter.ComediansViewHolder, position: Int) {
        holder.comName.text = comedians[position].name
        holder.voteBtn.setOnClickListener{
            listener.voted(comedians[position].name)
        }
    }

    inner class ComediansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val voteBtn = view.checkBtn
        val comName = view.comedianName

    }

    interface onVoteBtnClicked {
        fun voted(name: String)
    }
}