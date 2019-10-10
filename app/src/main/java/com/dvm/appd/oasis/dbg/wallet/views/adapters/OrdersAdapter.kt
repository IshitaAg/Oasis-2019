package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedOrdersData
import kotlinx.android.synthetic.main.adapter_order_items.view.*

class OrdersAdapter(private val listener:OrderCardClick): RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>(){

    var orderItems: List<ModifiedOrdersData> = emptyList()

    interface OrderCardClick{
        fun updateOtpSeen(orderId: Int)
        fun showOrderItemDialog(orderId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order_items, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int = orderItems.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {


        holder.stallName.text = orderItems[position].vendor
        holder.orderId.text = "${orderItems[position].orderId}"
        holder.price.text = "₹${orderItems[position].totalPrice}"

        when(orderItems[position].status){

            0 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_fade)
                holder.readyMark.setImageResource(R.drawable.ic_circle_fade)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_fade)
                holder.otp.isVisible = false
                holder.status.setBackgroundResource(R.drawable.pending_status)
                holder.status.text = "Pending"
            }
            1 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle)
                holder.readyMark.setImageResource(R.drawable.ic_circle_fade)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_fade)
                holder.otp.isVisible = false
                holder.status.setBackgroundResource(R.drawable.accept_status)
                holder.status.text = "Accepted"
            }
            2 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle)
                holder.readyMark.setImageResource(R.drawable.ic_circle)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_fade)
                holder.otp.isVisible = true
                holder.status.setBackgroundResource(R.drawable.ready_status)
                holder.status.text = "Ready"
            }
            3 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle)
                holder.readyMark.setImageResource(R.drawable.ic_circle)
                holder.finishedMark.setImageResource(R.drawable.ic_circle)
                holder.otp.isVisible = true
                holder.status.setBackgroundResource(R.drawable.finish_status)
                holder.status.text = "Finished"
            }
            4 -> {
                holder.acceptedMark.setImageResource(R.drawable.ic_circle_fade)
                holder.readyMark.setImageResource(R.drawable.ic_circle_fade)
                holder.finishedMark.setImageResource(R.drawable.ic_circle_fade)
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

        holder.view.setOnClickListener {
            listener.showOrderItemDialog(orderItems[position].orderId)
        }
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
        val otp: TextView = view.otp
        val acceptedMark: ImageView = view.acceptedMark
        val readyMark: ImageView = view.readyMark
        val finishedMark: ImageView = view.finishedMark
    }

}