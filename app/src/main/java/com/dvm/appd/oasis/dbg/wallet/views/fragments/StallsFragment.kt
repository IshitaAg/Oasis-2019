package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.StallData
import com.dvm.appd.oasis.dbg.wallet.viewmodel.StallsViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.StallsViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.StallResult
import com.dvm.appd.oasis.dbg.wallet.views.adapters.StallsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_wallet_stalls.view.*
import android.view.*
import android.widget.Toast
import androidx.lifecycle.MutableLiveData


class StallsFragment : Fragment(), StallsAdapter.OnStallSelectedListener {

    private lateinit var stallsViewModel: StallsViewModel
    private val picBaseUrl = "https://firebasestorage.googleapis.com/v0/b/bosm-2019.appspot.com/o/"
    private val stallImgs = listOf<String>(
        "${picBaseUrl}CCD.png?alt=media&token=cfd0822f-5204-43f0-880d-2e457238e8c3",
        "${picBaseUrl}Chaayos.png?alt=media&token=6253f2ac-f32c-4068-9eac-436267fc8261",
        "${picBaseUrl}Ho%20Ko%20Fo.png?alt=media&token=ab409a5d-1225-4077-98b1-e11bf9ad3e7a",
        "${picBaseUrl}Mr%20Crush.png?alt=media&token=47b89cca-53cb-457a-907d-ccc51277f0df",
        "${picBaseUrl}Keventer.png?alt=media&token=16d1f6fa-acb9-4236-af76-790e95706e86",
        "${picBaseUrl}Dominos.png?alt=media&token=201d0b7a-cb03-4131-9953-c3d9838f7326",
        "${picBaseUrl}Chaayos.png?alt=media&token=6253f2ac-f32c-4068-9eac-436267fc8261",
        "${picBaseUrl}Roll%20Point.png?alt=media&token=be4beac7-99f1-4258-b555-232c68806c34",
        "${picBaseUrl}Quick%20Meal.png?alt=media&token=0892fe39-9fc4-4a48-ab7a-7fca939760e5",
        "${picBaseUrl}Burger%20Talkies.png?alt=media&token=4155943d-4c8b-4452-a551-0761667c4bbb"
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        stallsViewModel = ViewModelProviders.of(this, StallsViewModelFactory())[StallsViewModel::class.java]

        val rootview = inflater.inflate(R.layout.fra_wallet_stalls, container, false)
        //activity!!.my_toolbar.setBackgroundResource(R.drawable.gradient_stalls_toolbar)
        rootview.stalls_recycler.adapter = StallsAdapter(this)

        stallsViewModel.stalls.observe(this, Observer {
            (rootview.stalls_recycler.adapter as StallsAdapter).stalls = it
            (rootview.stalls_recycler.adapter as StallsAdapter).stallImgs = stallImgs
            (rootview.stalls_recycler.adapter as StallsAdapter).notifyDataSetChanged()
        })

        stallsViewModel.result.observe(this, Observer {
            when (it!!) {
                StallResult.Success -> {
                    rootview.progressBar.visibility = View.GONE
                }
                StallResult.Failure -> {
                    rootview.progressBar.visibility = View.VISIBLE
                }
            }
        })

        stallsViewModel.error.observe(this, Observer {
            rootview.progressBar.visibility = View.GONE
            if (it != null) {
                Toast.makeText(context!!, it.toString(), Toast.LENGTH_SHORT).show()
                (stallsViewModel.error as MutableLiveData).postValue(null)
            }
        })

        return rootview
    }

        override fun stallSelected(stall: StallData) {
        val bundle = bundleOf("stallId" to stall.stallId, "stallName" to stall.stallName)
        view!!.findNavController().navigate(com.dvm.appd.oasis.dbg.R.id.action_action_food_to_stallItemsFragment2, bundle)
        // view!!.findNavController().navigate(StallsFragmentDirections.actionActionFoodToStallItemsFragment2(stallId))
    }

    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_stalls)
        super.onResume()
    }
}