
package com.dvm.appd.oasis.dbg.events.view.adapters


import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.adapter_misc_day.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class EventsDayAdapter(private val listener: OnDaySelected): RecyclerView.Adapter<EventsDayAdapter.EventsDayViewHolder>(){

    var days: List<String> = emptyList()
    var daySelected: String = "2019-10-19"

    interface OnDaySelected{
        fun daySelected(day: String, position: Int)
    }

    inner class EventsDayViewHolder(view: View): RecyclerView.ViewHolder(view){
        val day: TextView = view.day
        val underline: View = view.underline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsDayViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_misc_day, parent, false)

        return EventsDayViewHolder(view)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: EventsDayViewHolder, position: Int) {

        holder.day.text = "Day ${position}"

        if (days[position] == daySelected){
            holder.day.setTypeface(null, Typeface.BOLD)
            holder.underline.isVisible = true
            holder.underline.setBackgroundResource(R.color.white)
        }
        else{
            holder.day.setTypeface(null, Typeface.NORMAL)
            holder.underline.isVisible = false
        }

         holder.day.setOnClickListener {
             listener.daySelected(days[position], position)
         }

    }
}