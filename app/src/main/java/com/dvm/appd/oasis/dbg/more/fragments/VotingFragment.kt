package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.adapters.ComediansAdapter
import com.dvm.appd.oasis.dbg.more.viewmodel.VotingViewModel
import com.dvm.appd.oasis.dbg.more.viewmodel.VotingViewModelFactory
import kotlinx.android.synthetic.main.fra_voting.view.*

class VotingFragment:Fragment(),ComediansAdapter.onVoteBtnClicked{

    private val votingViewModel by lazy {
        ViewModelProviders.of(this,VotingViewModelFactory())[VotingViewModel::class.java]
    }
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
    override fun voted(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


