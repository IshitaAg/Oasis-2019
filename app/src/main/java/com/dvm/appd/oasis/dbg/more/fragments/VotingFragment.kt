package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.adapters.ComediansAdapter
import com.dvm.appd.oasis.dbg.more.viewmodel.VotingViewModel
import com.dvm.appd.oasis.dbg.more.viewmodel.VotingViewModelFactory
import kotlinx.android.synthetic.main.fra_voting.view.*

class VotingFragment : Fragment(), ComediansAdapter.onVoteBtnClicked {

    private val votingViewModel by lazy {
        ViewModelProviders.of(this, VotingViewModelFactory())[VotingViewModel::class.java]
    }
    var comedianName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fra_voting, container, false)
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        rootView.back.setOnClickListener {
            it.findNavController().popBackStack()
        }
        rootView.votingRecycler.adapter = ComediansAdapter(this)
        rootView.voteBtn.setOnClickListener {
            votingViewModel.vote(comedianName)
        }
        votingViewModel.comedians.observe(this, Observer {
            (rootView.votingRecycler.adapter as ComediansAdapter).comedians = it
            (rootView.votingRecycler.adapter as ComediansAdapter).notifyDataSetChanged()
        })

        votingViewModel.toast.observe(this, Observer {
            Toast.makeText(context!!,it!!,Toast.LENGTH_SHORT).show()
        })

        votingViewModel.voteState.observe(this, Observer {
            when(it!!){
                "Voted" -> {
                    rootView.textVote.visibility = View.VISIBLE
                    rootView.votingRecycler.visibility =View.INVISIBLE
                    rootView.textVote.text= "Voted Successfully!"
                }
                "Enabled" -> {
                    rootView.textVote.visibility = View.INVISIBLE
                    rootView.votingRecycler.visibility =View.VISIBLE
                }
                "Closed" -> {
                    rootView.textVote.visibility = View.VISIBLE
                    rootView.votingRecycler.visibility =View.INVISIBLE
                    rootView.textVote.text= "Voted is closed!"
                }
            }
        })
        return rootView
    }

    override fun voted(name: String) {
        comedianName = name
    }

}


