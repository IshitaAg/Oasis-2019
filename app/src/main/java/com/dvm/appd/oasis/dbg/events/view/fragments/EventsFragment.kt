package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.view.adapters.EventsAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_events_fragment.view.*

class EventsFragment : Fragment(), EventsAdapter.OnIconClicked{

    private lateinit var eventsViewViewModel: EventsViewModel
    private val icons = mapOf(
        "Football" to R.drawable.ic_football,
        "Basketball" to R.drawable.ic_basketball,
        "Tennis" to R.drawable.ic_tennis,
        "Hockey" to R.drawable.ic_hockey,
        "Squash" to R.drawable.ic_squash,
        "Volleyball" to R.drawable.ic_volleyball,
        "Cricket" to R.drawable.ic_cricket,
        "Athletics" to R.drawable.ic_racing,
        "Chess" to R.drawable.ic_chess,
        "Carrom" to R.drawable.ic_carrom,
        "Snooker" to R.drawable.ic_pool,
        "Pool" to R.drawable.ic_pool,
        "Powerlifting" to R.drawable.ic_weightlifting,
        "Body Building" to R.drawable.ic_body_building,
        "Taekwondo" to R.drawable.ic_karate,
        "Table Tennis" to R.drawable.ic_ping_pong,
        "Badminton" to R.drawable.ic_shuttlecock,
        "Frisbee" to R.drawable.ic_frisbee,
        "Swimming" to R.drawable.ic_swimsuit
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        eventsViewViewModel = ViewModelProviders.of(this, EventsViewModelFactory())[EventsViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_events_fragment, container, false)

        activity!!.mainView.setBackgroundResource(R.drawable.events_title)
        activity!!.fragmentName.text = "Events"
        view.progress_event.visibility = View.VISIBLE


        activity!!.cart.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_events_to_action_cart)
        }

        activity!!.profile.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_events_to_action_profile)
        }

        activity!!.notifications.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_events_to_notificationFragment)
        }

        view.eventsRecycler.adapter = EventsAdapter(icons, this)

        eventsViewViewModel.sportsName.observe(this, Observer {
            view.progress_event.visibility = View.INVISIBLE
            Log.d("EventsFrag", "Observed $it")
            activity!!.search.setAdapter(ArrayAdapter<String>(this.context!!, R.layout.search_dialog, R.id.suggestion, it.map {item -> item.event }))
            (view.eventsRecycler.adapter as EventsAdapter).sportsName = it
            (view.eventsRecycler.adapter as EventsAdapter).notifyDataSetChanged()
        })

        eventsViewViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (eventsViewViewModel.error as MutableLiveData).postValue(null)
            }
        })

        activity!!.search.threshold = 1
        activity!!.search.setOnItemClickListener { parent, searchView, position, id ->
            val name = parent.adapter.getItem(position) as String
            Log.d("Search", name)
            val bundle = bundleOf("name" to name)
            view.findNavController().navigate(R.id.sportsDataFragment, bundle)
        }

        view.miscEvents.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_action_events_to_miscEventsFragment, null))
        view.next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_action_events_to_miscEventsFragment, null))

     return view
     }

    override fun openSportsFragment(name: String) {
        val bundle = bundleOf("name" to name)
        view!!.findNavController().navigate(R.id.sportsDataFragment, bundle)


        //put navigation code here with name passed in openSportsFragment
    }

    override fun onHeartClick(sports: String, favMark: Int) {
        eventsViewViewModel.markFavourite(sports, favMark)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_events)
        activity!!.search.isVisible = true
        activity!!.textView7.isVisible = false
        activity!!.linearElasRecycler.isVisible = false
        activity!!.refresh.isVisible = false
        super.onResume()
    }
}