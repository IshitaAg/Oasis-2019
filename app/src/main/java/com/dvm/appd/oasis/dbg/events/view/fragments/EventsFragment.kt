package com.dvm.appd.oasis.dbg.events.view.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.view.adapters.EventsDayAdapter
import com.dvm.appd.oasis.dbg.events.view.adapters.EventsAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModelFactory
import kotlinx.android.synthetic.main.adapter_filter_item.view.*
import kotlinx.android.synthetic.main.fra_misc_events.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class EventsFragment : Fragment(), EventsAdapter.OnMarkFavouriteClicked, EventsDayAdapter.OnDaySelected {

    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var compareTime: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        eventsViewModel = ViewModelProviders.of(this, EventsViewModelFactory())[EventsViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_misc_events, container, false)

        val sdfDate = SimpleDateFormat("yyyy-MM-dd")
        val sdfTime = SimpleDateFormat("HH:mm")
        val time = Calendar.getInstance()
        time.add(Calendar.HOUR_OF_DAY, -2)
        compareTime = sdfTime.format(time.time)
        val date = Calendar.getInstance()

        when(sdfDate.format(date.time)){
            "2019-10-19" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-19")
                eventsViewModel.getEventData("2019-10-19")
            }

            "2019-10-20" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-20")
                eventsViewModel.getEventData("2019-10-20")
            }

            "2019-10-21" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-21")
                eventsViewModel.getEventData("2019-10-21")
            }

            "2019-10-22" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-22")
                eventsViewModel.getEventData("2019-10-22")
            }

            "2019-10-23" -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-23")
                eventsViewModel.getEventData("2019-10-23")
            }

            else -> {
                (eventsViewModel.daySelected as MutableLiveData).postValue("2019-10-19")
                eventsViewModel.getEventData("2019-10-19")
            }
        }

        view.dayRecycler.adapter = EventsDayAdapter(this)
        eventsViewModel.eventDays.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.dayRecycler.adapter as EventsDayAdapter).days = it
            (view.dayRecycler.adapter as EventsDayAdapter).notifyDataSetChanged()
        })

        view.miscEventRecycler.adapter = EventsAdapter(this)
        eventsViewModel.events.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.miscEventRecycler.adapter as EventsAdapter).events = it
            (view.miscEventRecycler.adapter as EventsAdapter).notifyDataSetChanged()
            val index = it.indexOfFirst { eventsData -> eventsData.time < compareTime }
            if (index > 0)
                view.miscEventRecycler.smoothScrollToPosition(index)
        })

        eventsViewModel.daySelected.observe(this, Observer {
            (view.dayRecycler.adapter as EventsDayAdapter).daySelected = it
            (view.dayRecycler.adapter as EventsDayAdapter).notifyDataSetChanged()
            (eventsViewModel.progressBarMark as MutableLiveData).postValue(0)
//            eventsViewModel.currentSubscription.dispose()
            eventsViewModel.getEventData(it.toString())
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



        view.filter.setOnClickListener {
            FilterDialog().also {
                it.arguments = bundleOf(("day" to (eventsViewModel.daySelected.value)))
            }.show(childFragmentManager, "FILTER")
        }

        return view
    }

    override fun daySelected(day: String, position: Int) {
        (eventsViewModel.daySelected as MutableLiveData).postValue(day)
        view!!.dayRecycler.smoothScrollToPosition(position)
    }

    override fun getDirections(venue: String) {



        val latitude = "28.3633546"
        val longitude = "75.5871163"
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude&mode=w")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            if (intent.resolveActivity(activity!!.packageManager) != null)
                startActivity(intent)
            else
                (eventsViewModel.error as MutableLiveData).postValue("No App to view directions")
        }
        catch (e: Exception){
            (eventsViewModel.error as MutableLiveData).postValue(e.message)
        }

    }

    override fun showAboutRules(about: String, rules: String) {

        val bundle = bundleOf("about" to about, "rules" to rules)
        view!!.findNavController().navigate(R.id.action_action_events_to_event_data, bundle)
    }

}
