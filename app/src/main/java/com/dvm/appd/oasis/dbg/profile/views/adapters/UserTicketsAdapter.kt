package com.dvm.appd.oasis.dbg.profile.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.UserShows
import kotlinx.android.synthetic.main.adapter_user_tickets.view.*

class UserTicketsAdapter: RecyclerView.Adapter<UserTicketsAdapter.UserTicketViewHolder>(){

    var userTickets: List<UserShows> = emptyList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserTicketViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.adapter_user_tickets, p0, false)
        return UserTicketViewHolder(view)
    }

    override fun getItemCount(): Int = userTickets.size

    override fun onBindViewHolder(p0: UserTicketViewHolder, p1: Int) {
        Log.d("TicketsUserA", "$userTickets")
        p0.showName.text = userTickets[p1].name
        p0.ticketUnused.text = userTickets[p1].unused.toString()
        p0.ticketCount.text = (userTickets[p1].unused + userTickets[p1].used).toString()
    }

    inner class UserTicketViewHolder(view: View): RecyclerView.ViewHolder(view){

        val showName: TextView = view.showName
        val ticketCount: TextView = view.ticketQuantity
        val ticketUnused: TextView = view.ticketUnused
    }
}