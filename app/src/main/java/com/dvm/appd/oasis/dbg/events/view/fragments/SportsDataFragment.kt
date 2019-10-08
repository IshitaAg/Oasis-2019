package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository.Keys.name
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.SportsData
import com.dvm.appd.oasis.dbg.events.view.adapters.GenderDataAdapter
import com.dvm.appd.oasis.dbg.events.view.adapters.SportsDataAdapter
import com.dvm.appd.oasis.dbg.events.viewmodel.SportsDataViewModel
import com.dvm.appd.oasis.dbg.events.viewmodel.SportsDataViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sports_data.*
import kotlinx.android.synthetic.main.fragment_sports_data.view.*
import kotlinx.android.synthetic.main.fragment_sports_data.view.recy_sports_vertical
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SportsDataFragment : Fragment(),GenderDataAdapter.OnGenderClicked, SportsDataAdapter.OnFavouriteClicked, AdapterView.OnItemSelectedListener {

    private var genderSelected=""
    private var genderWiseDataMap= mapOf<String,List<SportsData>>()
    private lateinit var currentTime: String
    private var filters = arrayListOf("Upcoming", "Over")
    private lateinit var filter: String
    private lateinit var sportsDataViewModel: SportsDataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var sportName= arguments?.getString("name")
        Log.d("Sports", "sports name selected: $name")
        sportsDataViewModel = ViewModelProviders.of(this, SportsDataViewModelFactory(sportName!!))[SportsDataViewModel::class.java]

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssss'Z'")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        currentTime = sdf.format(calendar.time)
        Log.d("Time", currentTime)
        val view = inflater.inflate(R.layout.fragment_sports_data, container, false)
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()

        view.filter.adapter = ArrayAdapter<String>(this.context!!, R.layout.filter_dialog, R.id.filterName, filters)
        view.filter.onItemSelectedListener = this

        view.textView4.text=sportName.capitalize()

        view.recy_sports_horizontal.adapter = GenderDataAdapter(genderSelected,this)
        view.recy_sports_vertical.adapter = SportsDataAdapter(this)

        sportsDataViewModel.gender.observe(this, Observer {
            Log.d("Sports11" , "Entered observer for gender Adapter with list = $it")
            (view.recy_sports_horizontal.adapter as GenderDataAdapter).gender = it
            (view.recy_sports_horizontal.adapter as GenderDataAdapter).notifyDataSetChanged()

            if (genderSelected.isEmpty()&&it.isNotEmpty())
            {
                genderSelected=it[0]
                setGenderWiseData()
            }
           // removeLoadingStateActivity()
            Log.d("SportsFRag", "Observed")

            /*(view.recy_sports_horizontal.adapter as GenderDataAdapter).genderSelected = genderSelected
            (view.recy_sports_horizontal.adapter as GenderDataAdapter).notifyDataSetChanged()

            (view.recy_sports_vertical.adapter as SportsDataAdapter).genderSelected=it[0]
            (view.recy_sports_vertical.adapter as SportsDataAdapter).notifyDataSetChanged()
*/
        })

        sportsDataViewModel.filter.observe(this, Observer {
            filter = it
            setGenderWiseData()
        })

        /*sportsDataViewModel.sportsData.observe(this, Observer {

            Log.d("SportsFRag", "Observed")
            (view.recy_sports_vertical.adapter as SportsDataAdapter).sportData = it[genderSelected] ?: error("Data for $genderSelected not found")
            (view.recy_sports_vertical.adapter as SportsDataAdapter).notifyDataSetChanged()
        })
        */

        sportsDataViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (sportsDataViewModel.error as MutableLiveData).postValue(null)
            }
        })

        sportsDataViewModel.epcIsABitch.observe(this, Observer {
            if (it != null){
                Log.d("Epc", it.toString())
                val bundle = bundleOf("description" to it.first, "link" to it.second)
                view.findNavController().navigate(R.id.action_action_sport_to_epc, bundle)
                (sportsDataViewModel.epcIsABitch as MutableLiveData).postValue(null)
            }
        })


        setGenderWiseDataObserver()

        view.back.setOnClickListener {
           it.findNavController().popBackStack()
        }

        return view
    }

    override fun genderClicked(gender: String) {
        genderSelected=gender
        // Toast.makeText(activity, "Gender selected$gender", Toast.LENGTH_LONG).show()
        setGenderWiseData()
        /*
        (recy_sports_vertical.adapter as SportsDataAdapter).genderSelected=gender
        (recy_sports_vertical.adapter as SportsDataAdapter).notifyDataSetChanged()
   */ }

    private fun setGenderWiseData(){
        try {

            //date wise data supplied
            // null agar hua tio dikkat nhi hobni chhiye shayd
            if (genderWiseDataMap[genderSelected].isNullOrEmpty())
            {
                // Toast.makeText(activity, "No data for $genderSelected", Toast.LENGTH_LONG).show()
                (recy_sports_vertical.adapter as SportsDataAdapter).sportData= emptyList()
            }
            else {
                Log.d("Time",
                    "${(genderWiseDataMap[genderSelected]
                        ?: error("")).map { it.time }}"
                )

                if (filter == "Upcoming") {
                    (recy_sports_vertical.adapter as SportsDataAdapter).sportData =
                        (genderWiseDataMap[genderSelected]
                            ?: error("No data Found $genderSelected")).filter { it.time >= currentTime }
                } else if (filter == "Over") {
                    (recy_sports_vertical.adapter as SportsDataAdapter).sportData =
                        (genderWiseDataMap[genderSelected]
                            ?: error("No data Found $genderSelected")).filter { it.time < currentTime }
                } else {
                    (recy_sports_vertical.adapter as SportsDataAdapter).sportData =
                        (genderWiseDataMap[genderSelected]
                            ?: error("No data Found $genderSelected"))
                }
            }
            //Check daywise data
            Log.d("CompletedDataCheck", "data Check${genderWiseDataMap[genderSelected]}.dayWiseorders.toString()}")
            (recy_sports_vertical.adapter as SportsDataAdapter).notifyDataSetChanged()
            //to change selected value of date
            (recy_sports_horizontal.adapter as GenderDataAdapter).genderSelected=genderSelected
            (recy_sports_horizontal.adapter as GenderDataAdapter).notifyDataSetChanged()

        } catch (e: Exception) {
            Log.d("CompletedError", e.toString())
            Snackbar.make(view!!.recy_sports_vertical, "Internal App Error. Please restart", Snackbar.LENGTH_INDEFINITE).show()
            // Toast.makeText(activity,"My message$e" , Toast.LENGTH_LONG).show()
        }
    }

    private fun setGenderWiseDataObserver() {
        sportsDataViewModel.sportsData.observe(this, Observer {
            Log.d("SportsFragment", "Observer Called With data = ${it.toString()}")
            genderWiseDataMap = it
            setGenderWiseData()
        })
    }

    override fun updateFavourite(matchNo: Int, favouriteMark: Int) {
        sportsDataViewModel.markMatchFavourite(matchNo, favouriteMark)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (sportsDataViewModel.filter as MutableLiveData).postValue(parent!!.adapter.getItem(position) as String)
        Log.d("Filter", filter)
    }

    override fun showEpcData(eventId: String) {
        sportsDataViewModel.getEpcData(eventId)
    }
}
