package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R

class ComediansAdapter(val listener:onVoteBtnClicked) : RecyclerView.Adapter<ComediansAdapter.ComediansViewHolder>() {

    val comedians: List<String> = emptyList()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ComediansViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }

    interface onVoteBtnClicked{
        fun voted(name:String)
    }
}