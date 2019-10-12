package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrderItemViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrderItemViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.OrderDialogAdapter
import kotlinx.android.synthetic.main.dia_order_details.view.*
import kotlinx.android.synthetic.main.dia_order_details.view.progressBar
import kotlinx.android.synthetic.main.fra_wallet_orders.*
import kotlinx.android.synthetic.main.fra_wallet_orders.view.*

class OrderItemsDialog: DialogFragment() {

    private lateinit var orderItemViewModel: OrderItemViewModel

//    override fun onStart() {
//        super.onStart()
//
//        orderDialog.minWidth = ((parentFragment!!.view!!.width)*.70).toInt()
//        orderDialog.minHeight = ((parentFragment!!.view!!.height))
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val orderId = arguments?.getInt("orderId")

        val view = inflater.inflate(R.layout.dia_order_details, container, false)

        orderItemViewModel = ViewModelProviders.of(this, OrderItemViewModelFactory(orderId!!))[OrderItemViewModel::class.java]

        view.items.adapter = OrderDialogAdapter()

        orderItemViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (orderItemViewModel.error as MutableLiveData).postValue(null)
            }
        })

        orderItemViewModel.progressBarMark.observe(this, Observer {
            if (it == 0){
                view.progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            else if (it == 1){
                view.progressBar.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        orderItemViewModel.order.observe(this, Observer {order ->

            Log.d("OrderDetailsView", order.toString())

            when(order.status){

                0 -> {
                    view.accepted.setImageResource(R.drawable.ic_circle_faded)
                    view.ready.setImageResource(R.drawable.ic_circle_faded)
                    view.finished.setImageResource(R.drawable.ic_circle_faded)
                    view.accToRed.setBackgroundResource(R.color.line_faded)
                    view.redToFin.setBackgroundResource(R.color.line_faded)
//                    view.status.setBackgroundResource(R.drawable.pending_status)
//                    view.status.text = "Pending"
                }
                1 -> {
                    view.accepted.setImageResource(R.drawable.ic_circle_filled)
                    view.ready.setImageResource(R.drawable.ic_circle_faded)
                    view.finished.setImageResource(R.drawable.ic_circle_faded)
                    view.accToRed.setBackgroundResource(R.color.line_faded)
                    view.redToFin.setBackgroundResource(R.color.line_faded)
//                    view.status.setBackgroundResource(R.drawable.accept_status)
//                    view.status.text = "Accepted"
                }
                2 -> {
                    view.accepted.setImageResource(R.drawable.ic_circle_filled)
                    view.ready.setImageResource(R.drawable.ic_circle_filled)
                    view.finished.setImageResource(R.drawable.ic_circle_faded)
                    view.accToRed.setBackgroundResource(R.color.line_filled)
                    view.redToFin.setBackgroundResource(R.color.line_faded)
//                    view.status.setBackgroundResource(R.drawable.ready_status)
//                    view.status.text = "Ready"
                }
                3 -> {
                    view.accepted.setImageResource(R.drawable.ic_circle_filled)
                    view.ready.setImageResource(R.drawable.ic_circle_filled)
                    view.finished.setImageResource(R.drawable.ic_circle_filled)
                    view.accToRed.setBackgroundResource(R.color.line_filled)
                    view.redToFin.setBackgroundResource(R.color.line_filled)
//                    view.status.setBackgroundResource(R.drawable.finish_status)
//                    view.status.text = "Finished"
                }
                4 -> {
                    view.accepted.setImageResource(R.drawable.ic_circle_faded)
                    view.ready.setImageResource(R.drawable.ic_circle_faded)
                    view.finished.setImageResource(R.drawable.ic_circle_faded)
                    view.accToRed.setBackgroundResource(R.color.line_faded)
                    view.redToFin.setBackgroundResource(R.color.line_faded)
//                    view.status.setBackgroundResource(R.drawable.decline_status)
//                    view.status.text = "Declined"
                }
            }

            if (order.otpSeen){
                view.otp.text = order.otp.toString()
                view.otp.isClickable = false
            }
            else{
                view.otp.text = "????"
                view.otp.setOnClickListener {
                    if (order.status == 2){
                        (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
                        orderItemViewModel.updateOtpSeen(order.orderId)
                    }
                    else{
                        Toast.makeText(context, "Order not yet ready", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            view.stallName.text = order.vendor
            view.orderId.text = "${order.orderId}"
            view.price.text = "₹ ${order.totalPrice}"


            if (order.status != 3){

                view.text.isVisible = false
                view.rating1.isVisible = false
                view.rating2.isVisible = false
                view.rating3.isVisible = false
                view.rating4.isVisible = false
                view.rating5.isVisible = false

                (view.items.adapter as OrderDialogAdapter).items = order.items
                (view.items.adapter as OrderDialogAdapter).notifyDataSetChanged()
            }
            else if (order.status == 3){

                (view.items.adapter as OrderDialogAdapter).items = order.items
                (view.items.adapter as OrderDialogAdapter).notifyDataSetChanged()

                when (order.rating) {

                    0 -> {

                        view.text.isVisible = true
                        view.rating1.isVisible = true
                        view.rating2.isVisible = true
                        view.rating3.isVisible = true
                        view.rating4.isVisible = true
                        view.rating5.isVisible = true

                        view.rating1.setImageResource(R.drawable.ic_star)
                        view.rating2.setImageResource(R.drawable.ic_star)
                        view.rating3.setImageResource(R.drawable.ic_star)
                        view.rating4.setImageResource(R.drawable.ic_star)
                        view.rating5.setImageResource(R.drawable.ic_star)

                        view.rating1.isClickable = true
                        view.rating2.isClickable = true
                        view.rating3.isClickable = true
                        view.rating4.isClickable = true
                        view.rating5.isClickable = true

                    }

                    else -> {

                        view.text.isVisible = false
                        view.rating1.isVisible = false
                        view.rating2.isVisible = false
                        view.rating3.isVisible = false
                        view.rating4.isVisible = false
                        view.rating5.isVisible = false

                        view.rating1.isClickable = false
                        view.rating2.isClickable = false
                        view.rating3.isClickable = false
                        view.rating4.isClickable = false
                        view.rating5.isClickable = false
                    }

                }
            }
        })

        view.rating1.setOnClickListener {
            view.rating1.setImageResource(R.drawable.ic_star_full)
            view.rating2.setImageResource(R.drawable.ic_star)
            view.rating3.setImageResource(R.drawable.ic_star)
            view.rating4.setImageResource(R.drawable.ic_star)
            view.rating5.setImageResource(R.drawable.ic_star)
            Log.d("Rating","Clicked")
            (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
            orderItemViewModel.rateOrder(orderItemViewModel.order.value!!.shell, 1)
        }

        view.rating2.setOnClickListener {
            view.rating1.setImageResource(R.drawable.ic_star_full)
            view.rating2.setImageResource(R.drawable.ic_star_full)
            view.rating3.setImageResource(R.drawable.ic_star)
            view.rating4.setImageResource(R.drawable.ic_star)
            view.rating5.setImageResource(R.drawable.ic_star)
            Log.d("Rating","Clicked")
            (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
            orderItemViewModel.rateOrder(orderItemViewModel.order.value!!.shell, 2)
        }

        view.rating3.setOnClickListener {
            view.rating1.setImageResource(R.drawable.ic_star_full)
            view.rating2.setImageResource(R.drawable.ic_star_full)
            view.rating3.setImageResource(R.drawable.ic_star_full)
            view.rating4.setImageResource(R.drawable.ic_star)
            view.rating5.setImageResource(R.drawable.ic_star)
            Log.d("Rating","Clicked")
            (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
            orderItemViewModel.rateOrder(orderItemViewModel.order.value!!.shell, 3)
        }

        view.rating4.setOnClickListener {
            view.rating1.setImageResource(R.drawable.ic_star_full)
            view.rating2.setImageResource(R.drawable.ic_star_full)
            view.rating3.setImageResource(R.drawable.ic_star_full)
            view.rating4.setImageResource(R.drawable.ic_star_full)
            view.rating5.setImageResource(R.drawable.ic_star)
            Log.d("Rating","Clicked")
            (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
            orderItemViewModel.rateOrder(orderItemViewModel.order.value!!.shell, 4)
        }

        view.rating5.setOnClickListener {
            view.rating1.setImageResource(R.drawable.ic_star_full)
            view.rating2.setImageResource(R.drawable.ic_star_full)
            view.rating3.setImageResource(R.drawable.ic_star_full)
            view.rating4.setImageResource(R.drawable.ic_star_full)
            view.rating5.setImageResource(R.drawable.ic_star_full)
            Log.d("Rating","Clicked")
            (orderItemViewModel.progressBarMark as MutableLiveData).postValue(0)
            orderItemViewModel.rateOrder(orderItemViewModel.order.value!!.shell, 5)
        }

        return view
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        return dialog
//    }

}