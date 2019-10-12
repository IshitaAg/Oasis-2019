package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.views.adapters.KindItemsAdapter
import kotlinx.android.synthetic.main.fra_wallet_stall_items.view.*
import kotlin.reflect.KParameter

class KindItemsFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fra_wallet_stall_items,container,false)
        rootView.items_recycler.adapter = KindItemsAdapter()

        return rootView
    }
}