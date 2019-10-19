package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedOrdersData
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.adapter_order_items.view.*
import java.util.concurrent.TimeUnit

class OrdersAdapter(private val listener:OrderCardClick): RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>(){

    var orderItems: List<ModifiedOrdersData> = emptyList()

    interface OrderCardClick{
        fun updateOtpSeen(orderId: Int)
        fun showOrderItemDialog(orderId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order_items, parent, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.view.background = ContextCompat.getDrawable(parent.context, R.drawable.ic_order_back)
        }
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int = orderItems.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {


        holder.stallName.text = orderItems[position].vendor
        holder.orderId.text = "${orderItems[position].orderId}"
        holder.price.text = "₹${orderItems[position].totalPrice}"

        when(orderItems[position].status){

            0 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.readyMark.setImageResource(R.drawable.ic_circle_faded)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.otp.isVisible = false
                holder.status.setBackgroundResource(R.drawable.pending_status)
                holder.status.text = "Pending"
            }
            1 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_filled)
                holder.readyMark.setImageResource(R.drawable.ic_circle_faded)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.otp.isVisible = false
                holder.status.setBackgroundResource(R.drawable.accept_status)
                holder.status.text = "Accepted"
            }
            2 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_filled)
                holder.readyMark.setImageResource(R.drawable.ic_circle_filled)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.otp.isVisible = true
                holder.status.setBackgroundResource(R.drawable.ready_status)
                holder.status.text = "Ready"
            }
            3 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_filled)
                holder.readyMark.setImageResource(R.drawable.ic_circle_filled)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_filled)
                holder.otp.isVisible = true
                holder.status.setBackgroundResource(R.drawable.finish_status)
                holder.status.text = "Finished"
            }
            4 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.readyMark.setImageResource(R.drawable.ic_circle_faded)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_faded)
                holder.otp.isVisible = false
                holder.status.setBackgroundResource(R.drawable.decline_status)
                holder.status.text = "Declined"
            }
        }

        if (orderItems[position].otpSeen){
            holder.otp.text = orderItems[position].otp.toString()
            holder.otp.isClickable = false
        }
        else{
            holder.otp.setOnClickListener {
                if (orderItems[position].status == 2){
                    listener.updateOtpSeen(orderItems[position].orderId)
                    Log.d("OTP", "Called")
                }
                else{
                    Log.d("OTP", "Status not yet ready ${orderItems[position].status}")
                }
            }
        }

        RxView.clicks(holder.view).debounce(500,TimeUnit.MILLISECONDS).subscribe({
            listener.showOrderItemDialog(orderItems[position].orderId)
        },{
            Log.d("checke",it.toString())
        })
       /* holder.view.setOnClickListener {

        }*/
    }

    override fun onViewRecycled(holder: OrdersViewHolder) {
        super.onViewRecycled(holder)

        holder.otp.text = "OTP"
    }

    inner class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view){

        val stallName: TextView = view.stallName
        val status: TextView = view.status
        val orderId: TextView = view.orderId
        val price: TextView = view.price
        val view: View = view.view
        val otp: TextView = view.text_otp
        val acceptedMark: ImageView = view.acceptedMark
        val readyMark: ImageView = view.readyMark
        val finishedMark: ImageView = view.finishedMark
    }

}