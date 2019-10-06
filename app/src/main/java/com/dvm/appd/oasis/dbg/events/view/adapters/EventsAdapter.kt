package com.dvm.appd.oasis.dbg.events.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.EventsData
import kotlinx.android.synthetic.main.adapter_events_fragment.view.*

class EventsAdapter(private val icons: Map<String, Int>, private val listener: OnIconClicked): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(){

    var sportsName: List<EventsData> = emptyList()

    interface OnIconClicked{
        fun openSportsFragment(name: String)
        fun onHeartClick(sports: String, favMark: Int)
    }

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val sportsName: TextView = view.sportsEvents
        val icon: ImageView = view.icon
        val heart: ImageView = view.heart
        val next: ImageView = view.next
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_events_fragment, parent, false)
        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int = sportsName.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.sportsName.text = sportsName[position].event
        if (icons[sportsName[position].event] != null){
            holder.icon.setImageResource(icons[sportsName[position].event]!!)
        }
        holder.sportsName.setOnClickListener {
            listener.openSportsFragment(sportsName[position].event)
        }

        holder.next.setOnClickListener {
            listener.openSportsFragment(sportsName[position].event)
        }

        if (sportsName[position].isFav == 1){
            holder.heart.setImageResource(R.drawable.ic_heart_full)
        }else if (sportsName[position].isFav == 0){
            holder.heart.setImageResource(R.drawable.ic_heart)
        }

        holder.heart.setOnClickListener {

            if (sportsName[position].isFav == 1){
                listener.onHeartClick(sportsName[position].event, 0)
            }
            else if (sportsName[position].isFav == 0){
                listener.onHeartClick(sportsName[position].event, 1)
            }
        }
    }

}