package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.CartData
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedStallItemsData
import com.dvm.appd.oasis.dbg.wallet.viewmodel.StallItemsViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.StallItemsViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.StallItemsAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.StallItemsChildAdapter
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.*
import java.util.concurrent.TimeUnit

class StallItemsFragment : Fragment(), StallItemsChildAdapter.OnAddClickedListener {

    private lateinit var stallItemsViewModel: StallItemsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val stallId = arguments?.getInt("stallId")
        val stallName = arguments?.getString("stallName")

        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()

        val rootView = inflater.inflate(R.layout.fra_wallet_stall_items, container, false)

        rootView.stallName.text = stallName

        stallItemsViewModel = ViewModelProviders.of(this, StallItemsViewModelFactory(stallId!!))[StallItemsViewModel::class.java]

        rootView.items_recycler.adapter = StallItemsAdapter(this)

        stallItemsViewModel.items.observe(this, Observer {
            (rootView.items_recycler.adapter as StallItemsAdapter).items = it
            (rootView.items_recycler.adapter as StallItemsAdapter).notifyDataSetChanged()
        })

        stallItemsViewModel.cartItems.observe(this, Observer {

            if (it.sumBy {it1 -> it1.second.sumBy {it2 -> it2.quantity * it2.currentPrice}} != 0){
                rootView.stallOrderView.isVisible = true
                rootView.imageView5.isVisible = true
                rootView.viewCart.text = "View Cart"
                rootView.totalPrice.text = "₹ ${it.sumBy {it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.currentPrice}}}"
                rootView.itemCount.text = "${it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity }}} items"
            }
            else{
                rootView.totalPrice.text = ""
                rootView.itemCount.text = ""
                rootView.viewCart.text = ""
                rootView.stallOrderView.isVisible = false
                rootView.imageView5.isVisible = false
            }
        })

        stallItemsViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (stallItemsViewModel.error as MutableLiveData).postValue(null)
            }
        })

        rootView.stallOrderView.setOnClickListener {
            this.findNavController().navigate(R.id.action_stallItemsFragment2_to_action_cart)
            //TODO add navigation to cart fragment
        }

        rootView.backBtn.setOnClickListener {
            rootView.backBtn.findNavController().popBackStack()
        }
        return rootView
    }



    override fun addButtonClicked(stallItem: ModifiedStallItemsData, quantity: Int) {

        stallItemsViewModel.insertCartItems(CartData(stallItem.itemId, quantity, stallItem.stallId))
    }

    override fun deleteCartItemClicked(itemId: Int) {

        stallItemsViewModel.deleteCartItem(itemId)
    }
}