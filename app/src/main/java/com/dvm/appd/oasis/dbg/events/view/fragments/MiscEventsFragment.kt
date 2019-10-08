package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.view.adapters.MiscDayAdapter
import com.dvm.appd.oasis.dbg.events.view.adapters.MiscEventsAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.MiscEventsViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.MiscEventsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_misc_events.view.*
import java.text.SimpleDateFormat
import java.util.*

class MiscEventsFragment : Fragment(), MiscEventsAdapter.OnMarkFavouriteClicked, MiscDayAdapter.OnDaySelected {

    private lateinit var miscEventsViewViewModel: MiscEventsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        miscEventsViewViewModel = ViewModelProviders.of(this, MiscEventsViewModelFactory())[MiscEventsViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_misc_events, container, false)

        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()

        val sdf = SimpleDateFormat("dd MM yyyy")
        val c = Calendar.getInstance()

        when(sdf.format(c.time)){
            "13 09 2019" -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 0")
                miscEventsViewViewModel.getMiscEventsData("Day 0")
            }

            "14 09 2019" -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 1")
                miscEventsViewViewModel.getMiscEventsData("Day 1")
            }

            "15 09 2019" -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 2")
                miscEventsViewViewModel.getMiscEventsData("Day 2")
            }

            "16 09 2019" -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 3")
                miscEventsViewViewModel.getMiscEventsData("Day 3")
            }

            "17 09 2019" -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 4")
                miscEventsViewViewModel.getMiscEventsData("Day 4")
            }

            else -> {
                (miscEventsViewViewModel.daySelected as MutableLiveData).postValue("Day 0")
                miscEventsViewViewModel.getMiscEventsData("Day 0")
            }
        }


        view.dayRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.dayRecycler.adapter = MiscDayAdapter(this)
        miscEventsViewViewModel.eventDays.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.dayRecycler.adapter as MiscDayAdapter).miscDays = it
            (view.dayRecycler.adapter as MiscDayAdapter).notifyDataSetChanged()
        })

        view.miscEventRecycler.adapter = MiscEventsAdapter(this)
        miscEventsViewViewModel.miscEvents.observe(this, Observer {
            Log.d("MiscEventsFrag", "Observed")
            (view.miscEventRecycler.adapter as MiscEventsAdapter).miscEvents = it
            (view.miscEventRecycler.adapter as MiscEventsAdapter).notifyDataSetChanged()
        })

        miscEventsViewViewModel.daySelected.observe(this, Observer {

            (view.dayRecycler.adapter as MiscDayAdapter).daySelected = it
            (view.dayRecycler.adapter as MiscDayAdapter).notifyDataSetChanged()
        })

        miscEventsViewViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (miscEventsViewViewModel.error as MutableLiveData).postValue(null)
            }
        })

       /* view.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }*/

        miscEventsViewViewModel.epcIsABitch.observe(this, Observer {

            if (it != null) {
                Log.d("Epc", it.toString())
                val bundle = bundleOf("description" to it.first, "link" to it.second)
                view.findNavController().navigate(R.id.action_action_misc_to_epc, bundle)
                (miscEventsViewViewModel.epcIsABitch as MutableLiveData).postValue(null)

            }
        })

        return view
    }

    override fun updateIsFavourite(eventId: String, favouriteMark: Int) {
        miscEventsViewViewModel.markEventFavourite(eventId, favouriteMark)
    }

    override fun daySelected(day: String, position: Int) {
        (miscEventsViewViewModel.daySelected as MutableLiveData).postValue(day)
        miscEventsViewViewModel.currentSubsciption.dispose()
        miscEventsViewViewModel.getMiscEventsData(day)
        view!!.dayRecycler.smoothScrollToPosition(position)
    }

    override fun showEpcData(eventId: String) {
        miscEventsViewViewModel.getEpcData(eventId)
    }
}
