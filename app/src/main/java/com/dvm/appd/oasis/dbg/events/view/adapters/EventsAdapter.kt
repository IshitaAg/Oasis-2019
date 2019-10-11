package com.dvm.appd.oasis.dbg.events.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.ModifiedEventsData
import kotlinx.android.synthetic.main.adapter_misc_events.view.*
import java.lang.Exception

class EventsAdapter(private val listener: OnMarkFavouriteClicked): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(){

    var events: List<ModifiedEventsData> = emptyList()

    interface OnMarkFavouriteClicked{
        fun updateIsFavourite(eventId: Int, favouriteMark: Int)
    }

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val event: TextView = view.eventName
        val description: TextView = view.eventDesc
        val organiser: TextView = view.eventOrg
        val time: TextView = view.eventTime
        val venue: TextView = view.eventVenue
        val markFav: ImageView = view.markFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_misc_events, parent, false)
        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {


        holder.event.text = events[position].name
        holder.description.text = events[position].details
        holder.organiser.text = events[position].duration
        holder.time.text = events[position].dateTime //getTime(events[position].time)
        holder.venue.text = events[position].venue.first()
        if (events[position].isFav == 1){
            holder.markFav.setImageResource(R.drawable.ic_is_favourite)
        }else if (events[position].isFav == 0){
            holder.markFav.setImageResource(R.drawable.ic_not_favourite)
        }

        holder.markFav.setOnClickListener {

            if (events[position].isFav == 1){
                listener.updateIsFavourite(events[position].eventId, 0)
            }
            else if (events[position].isFav == 0){
                listener.updateIsFavourite(events[position].eventId, 1)
            }
        }
    }

    private fun getTime(datetime: String): String {

        return try {
            datetime.substring(11, 16)
        }catch (e: Exception){
            datetime
        }
    }

}