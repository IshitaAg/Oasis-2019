package com.dvm.appd.oasis.dbg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.PaytmRoom
import kotlinx.android.synthetic.main.card_paytm_logs.view.*

class PaytmLogsAdapter: RecyclerView.Adapter<PaytmLogsAdapter.PaytmLogsAdapterViewHolder>() {
    var list: List<PaytmRoom> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaytmLogsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_paytm_logs, parent, false)
        return PaytmLogsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PaytmLogsAdapterViewHolder, position: Int) {
        holder.textView.text = list[position].toString()
    }

    inner class PaytmLogsAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView = view.text_payTm_log
    }
}