package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.view.adapters.EventsDayAdapter
import com.dvm.appd.oasis.dbg.events.view.adapters.EventsAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModelFactory
import kotlinx.android.synthetic.main.fra_misc_events.view.*
import java.text.SimpleDateFormat
import java.util.*

class EventsFragment : Fragment(), EventsAdapter.OnMarkFavouriteClicked, EventsDayAdapter.OnDaySelected {

    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        eventsViewModel = ViewModelProviders.of(this, EventsViewModelFactory())[EventsViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_misc_events, container, false)
        
        val sdf = SimpleDateFormat("dd MM yyyy")
        val c = Calendar.getInstance()

        when(sdf.format(c.time)){
            "19 10 2019" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 0")
                eventsViewModel.getMiscEventsData("Day 0")
            }

            "20 10 2019" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 1")
                eventsViewModel.getMiscEventsData("Day 1")
            }

            "21 10 2019" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 2")
                eventsViewModel.getMiscEventsData("Day 2")
            }

            "22 10 2019" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 3")
                eventsViewModel.getMiscEventsData("Day 3")
            }

            "23 10 2019" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 4")
                eventsViewModel.getMiscEventsData("Day 4")
            }

            else -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("Day 0")
                eventsViewModel.getMiscEventsData("Day 0")
            }
        }


        view.dayRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.dayRecycler.adapter = EventsDayAdapter(this)
        eventsViewModel.eventDays.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.dayRecycler.adapter as EventsDayAdapter).miscDays = it
            (view.dayRecycler.adapter as EventsDayAdapter).notifyDataSetChanged()
        })

        view.miscEventRecycler.adapter = EventsAdapter(this)
        eventsViewModel.events.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.miscEventRecycler.adapter as EventsAdapter).events = it
            (view.miscEventRecycler.adapter as EventsAdapter).notifyDataSetChanged()
        })

        eventsViewModel.daySelected.observe(this, Observer {

            (view.dayRecycler.adapter as EventsDayAdapter).daySelected = it
            (view.dayRecycler.adapter as EventsDayAdapter).notifyDataSetChanged()
        })

        eventsViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (eventsViewModel.error as MutableLiveData).postValue(null)
            }
        })

        eventsViewModel.progressBarMark.observe(this, Observer {
            if (it == 0){
                view.eventProgress.isVisible = true
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            else if (it == 1){
                view.eventProgress.isVisible = false
                view.swipeEvents.isRefreshing = false
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        view.swipeEvents.setOnRefreshListener {
            (eventsViewModel.progressBarMark as MutableLiveData).postValue(0)
            eventsViewModel.refreshData()
        }

        return view
    }

    override fun updateIsFavourite(eventId: Int, favouriteMark: Int) {
        (eventsViewModel.progressBarMark as MutableLiveData).postValue(0)
        eventsViewModel.markEventFav(eventId, favouriteMark)
    }

    override fun daySelected(day: String, position: Int) {
        (eventsViewModel.daySelected as MutableLiveData).postValue(day)
        eventsViewModel.currentSubscription.dispose()
        (eventsViewModel.progressBarMark as MutableLiveData).postValue(0)
        eventsViewModel.getMiscEventsData(day)
        view!!.dayRecycler.smoothScrollToPosition(position)
    }
}
