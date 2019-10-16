package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.profile.viewmodel.TicketViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.TicketViewModelFactory
import com.dvm.appd.oasis.dbg.profile.views.adapters.TicketsAdapter
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.TicketsCart
import kotlinx.android.synthetic.main.dia_tickets.view.*

class TicketDialog : DialogFragment(), TicketsAdapter.TicketCartActions{

    private lateinit var ticketsViewModel: TicketViewModel

//    override fun onStart() {
//        super.onStart()
//
//        ticketsDialog.minWidth = ((parentFragment!!.view!!.width)*.85).toInt()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dia_tickets, container, false)

        ticketsViewModel = ViewModelProviders.of(this, TicketViewModelFactory())[TicketViewModel::class.java]

        view.ticketsList.adapter = TicketsAdapter(this)

        ticketsViewModel.tickets.observe(this, Observer {
            Log.d("FUCK","₹${it.sumBy {item -> item.price * item.quantity}}")
            view.tPrice.text = "₹${it.sumBy {item -> item.price * item.quantity}}"
            (view.ticketsList.adapter as TicketsAdapter).tickets = it
            (view.ticketsList.adapter as TicketsAdapter).notifyDataSetChanged()
        })

        ticketsViewModel.error.observe(this, Observer {
            if (it != null) {
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (ticketsViewModel.error as MutableLiveData).postValue(null)
            }
        })

        ticketsViewModel.progressBarMark.observe(this, Observer {

            if (it == 0 ){
                view.progressBar2.visibility = View.VISIBLE
                view.button.isClickable = false
            }
            else if (it == 1){
                view.progressBar2.visibility = View.GONE
                view.button.isClickable = true
            }
        })

        //Add progress bar

        view.button.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Buy Ticket(s)?")
                .setPositiveButton("OK") { _, _ ->
                    (ticketsViewModel.progressBarMark as MutableLiveData).postValue(0)
                    ticketsViewModel.buyTickets()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

        ticketsViewModel.redirect.observe(this, Observer {
            if (it){
                (ticketsViewModel.redirect as MutableLiveData).postValue(false)
                view.findNavController().popBackStack()
            }
        })

        return view
    }

    override fun insertTicketCart(ticket: TicketsCart) {
        ticketsViewModel.insertTicketCart(ticket)
    }

    override fun updateTicketCart(quantity: Int, id: Int) {
        ticketsViewModel.updateTicketCart(quantity, id)
    }

    override fun deleteTicketCart(id: Int) {
        ticketsViewModel.deleteTiceketCartItem(id)
    }

    override fun onResume() {
        super.onResume()

        (ticketsViewModel.redirect as MutableLiveData).postValue(false)
    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        return dialog
//
//    }
}