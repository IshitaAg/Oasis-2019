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
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.*


class KindItemsFragment:Fragment() {
    private val kindItemsViewModel by lazy{
        ViewModelProviders.of(this,KindItemsViewModelFactory())[KindItemsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fra_wallet_stall_items,container,false)
        rootView.items_recycler.adapter = KindItemsAdapter()
        rootView.stallName.text = "Kind Store"
        rootView.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        kindItemsViewModel.toast.observe(this, Observer {
            Toast.makeText(context!!,it!!,Toast.LENGTH_SHORT).show()
        })
        kindItemsViewModel.items.observe(this, Observer {
            (rootView.items_recycler.adapter as KindItemsAdapter).items = it!!
            (rootView.items_recycler.adapter as KindItemsAdapter).notifyDataSetChanged()

        })
        return rootView
    }
}