package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.viewmodel.KindItemsViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.KindItemsViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.KindItemsAdapter
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.fra_kind_items.view.*
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.*
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.backBtn
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.stallName
import java.util.concurrent.TimeUnit


class KindItemsFragment:Fragment() {
    private val kindItemsViewModel by lazy{
        ViewModelProviders.of(this,KindItemsViewModelFactory())[KindItemsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fra_kind_items,container,false)
        rootView.stallName.text = "Kind Store"
        rootView.setOnClickListener {
            view!!.findNavController().popBackStack()

        }
        kindItemsViewModel.toast.observe(this, Observer {
            Toast.makeText(context!!,it!!,Toast.LENGTH_SHORT).show()
        })
        rootView.itemsRecycler.adapter = KindItemsAdapter()
        kindItemsViewModel.items.observe(this, Observer {
            (rootView.itemsRecycler.adapter as KindItemsAdapter).items = it!!
            (rootView.itemsRecycler.adapter as KindItemsAdapter).notifyDataSetChanged()

        })
        return rootView
    }
}