package com.dvm.appd.oasis.dbg.profile.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedTicketsData
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.TicketsCart
import kotlinx.android.synthetic.main.adapter_tickets.view.*

class TicketsAdapter(private val listener: TicketCartActions): RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>(){

    var tickets: List<ModifiedTicketsData> = emptyList()

    interface TicketCartActions{
        fun insertTicketCart(ticket: TicketsCart)
        fun updateTicketCart(quantity: Int, id: Int)
        fun deleteTicketCart(id: Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TicketsViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.adapter_tickets, p0, false)
        return TicketsViewHolder(view)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(p0: TicketsViewHolder, p1: Int) {

        p0.name.text = tickets[p1].name
        p0.price.text = "₹${tickets[p1].price}"
        p0.shows.isVisible = false

        if (tickets[p1].quantity > 0){
            p0.add.isVisible = false
            p0.minus.isVisible = true
            p0.plus.isVisible = true
            p0.quantity.isVisible = true

            p0.quantity.text = tickets[p1].quantity.toString()

            p0.plus.setOnClickListener {
                listener.updateTicketCart(tickets[p1].quantity + 1, tickets[p1].cartId)
            }

            p0.minus.setOnClickListener {

                if (tickets[p1].quantity > 1){
                    listener.updateTicketCart(tickets[p1].quantity - 1, tickets[p1].cartId)
                }
                else{
                    listener.deleteTicketCart(tickets[p1].cartId)
                }
            }
        }
        else{
            p0.add.isVisible = true
            p0.minus.isVisible = false
            p0.plus.isVisible = false
            p0.quantity.isVisible = false

            p0.add.setOnClickListener {
                listener.insertTicketCart(TicketsCart(tickets[p1].ticketId, 1, tickets[p1].type, 0))
            }
        }

    }

    inner class TicketsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val name: TextView = view.name
        val shows: TextView = view.shows
        val price: TextView = view.price
        val add: Button = view.addBtn
        val quantity: TextView = view.quantity
        val plus: Button = view.plus
        val minus: Button = view.minus
    }
}