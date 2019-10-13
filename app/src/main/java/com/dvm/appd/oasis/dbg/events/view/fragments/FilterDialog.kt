package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.view.adapters.FilterAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.EventsViewModelFactory
import com.dvm.appd.oasis.dbg.shared.util.asMut
import kotlinx.android.synthetic.main.dia_filter_events.view.*

class FilterDialog: DialogFragment(), FilterAdapter.DialogItemTouch{

    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dia_filter_events, container, false)

        eventsViewModel = ViewModelProviders.of(this, EventsViewModelFactory())[EventsViewModel::class.java]

        view.filterRecycler.adapter = FilterAdapter(this)
        eventsViewModel.categories.observe(this, Observer {
            (view.filterRecycler.adapter as FilterAdapter).categories = it
            (view.filterRecycler.adapter as FilterAdapter).notifyDataSetChanged()
        })

        return view
    }

    override fun updateFilter(category: String, filter: Boolean) {
        eventsViewModel.updatedFilters(category, filter)
    }
}