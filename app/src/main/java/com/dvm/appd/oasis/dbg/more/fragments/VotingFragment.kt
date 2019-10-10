package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.fra_voting.view.*

class VotingFragment:Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fra_voting,container,false)
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        rootView.back.setOnClickListener {
            it.findNavController().popBackStack()
        }

        return rootView
    }
}


