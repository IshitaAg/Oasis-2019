package com.dvm.appd.oasis.dbg.events.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.MiscEventsData
import kotlinx.android.synthetic.main.adapter_misc_events.view.*
import java.lang.Exception

class EventsAdapter(private val listener: OnMarkFavouriteClicked): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(){

    var miscEvents: List<MiscEventsData> = emptyList()

    interface OnMarkFavouriteClicked{
        fun updateIsFavourite(eventId: String, favouriteMark: Int)
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

    override fun getItemCount(): Int = miscEvents.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {


        holder.event.text = miscEvents[position].name
        holder.description.text = miscEvents[position].description
        holder.organiser.text = miscEvents[position].organiser
        holder.time.text = getTime(miscEvents[position].time)
        holder.venue.text = miscEvents[position].venue
        if (miscEvents[position].isFavourite == 1){
            holder.markFav.setImageResource(R.drawable.ic_is_favourite)
        }else if (miscEvents[position].isFavourite == 0){
            holder.markFav.setImageResource(R.drawable.ic_not_favourite)
        }

        holder.markFav.setOnClickListener {

            if (miscEvents[position].isFavourite == 1){
                listener.updateIsFavourite(miscEvents[position].id, 0)
            }
            else if (miscEvents[position].isFavourite == 0){
                listener.updateIsFavourite(miscEvents[position].id, 1)
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