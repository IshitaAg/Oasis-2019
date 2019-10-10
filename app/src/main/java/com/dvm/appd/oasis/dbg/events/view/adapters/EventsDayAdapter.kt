
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
import kotlinx.android.synthetic.main.adapter_misc_day.view.*

class EventsDayAdapter(private val listener: OnDaySelected): RecyclerView.Adapter<EventsDayAdapter.EventsDayViewHolder>(){

    var miscDays: List<String> = emptyList()
    var daySelected: String = ""

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

    override fun getItemCount(): Int = miscDays.size

    override fun onBindViewHolder(holder: EventsDayViewHolder, position: Int) {

        holder.day.text = miscDays[position]

        if (miscDays[position] == daySelected){
            holder.day.setTextColor(Color.rgb(104, 81, 218))
            holder.day.setTypeface(null, Typeface.BOLD)
            holder.underline.isVisible = true
            holder.underline.setBackgroundResource(R.color.white)
        }
        else{
            holder.day.setTextColor(Color.rgb(137, 134, 134))
            holder.day.setTypeface(null, Typeface.NORMAL)
            holder.underline.isVisible = false
        }

        holder.day.setOnClickListener {
            listener.daySelected(miscDays[position], position)
        }
    }
}