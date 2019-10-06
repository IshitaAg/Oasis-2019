package com.dvm.appd.oasis.dbg.elas.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.UIStateElas
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.dvm.appd.oasis.dbg.elas.model.retrofit.PlayerRankingResponse
import com.dvm.appd.oasis.dbg.elas.view.adapter.ELasLeaderoardAdapter
import com.dvm.appd.oasis.dbg.elas.view.adapter.ElasQuestionsAdapter
import com.dvm.appd.oasis.dbg.elas.viewModel.ElasViewModel
import com.dvm.appd.oasis.dbg.elas.viewModel.ElasViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_elas_fragment.*
import java.lang.Exception

class ELASFragment : Fragment(), ElasQuestionsAdapter.onQuestionButtonClicked {

    private val TAG = "ELAS Fragment"

    private val elasViewModel by lazy {
        ViewModelProviders.of(this, ElasViewModelFactory())[ElasViewModel::class.java]
    }
    var currentLeaderboardList = emptyList<PlayerRankingResponse>()
    var currentQuestionsList: Map<Long, List<CombinedQuestionOptionDataClass>> = emptyMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity!!.mainView.setBackgroundResource(R.drawable.quiz_title)
        selectQuestions()
        return inflater.inflate(R.layout.fra_elas_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        activity!!.bttn_Questions_elas.setOnClickListener {
            Log.d("ElasFragment", "Questions Selected")
            selectQuestions()
        }

        activity!!.bttn_Leaderboard_elas.setOnClickListener {
            Log.d("ElasFragment", "Options Selected")
            selectLeaderboard()
        }

        elasViewModel.uiState.observe(this, Observer {
            when(it!!) {
                UIStateElas.Loading -> {
                    progress_fra_elas.visibility = View.VISIBLE
                    activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                is UIStateElas.Failure -> {
                    progress_fra_elas.visibility = View.INVISIBLE
                    activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Snackbar.make(activity!!.coordinator_parent, (it as UIStateElas.Failure).message, Snackbar.LENGTH_SHORT).show()
                }
                is UIStateElas.Questions -> {
                    progress_fra_elas.visibility = View.INVISIBLE
                    activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    currentQuestionsList = (it as UIStateElas.Questions).questions
                    if (recycler_elasFrag_questions.adapter is ElasQuestionsAdapter) {
                        (recycler_elasFrag_questions.adapter as ElasQuestionsAdapter).questionsList = (it as UIStateElas.Questions).questions.toSortedMap(reverseOrder())
                        (recycler_elasFrag_questions.adapter as ElasQuestionsAdapter).notifyDataSetChanged()
                    }
                }
            }
        })

        elasViewModel.leaderboard.observe(this, Observer {
            if (it.isNotEmpty() && recycler_elasFrag_questions.adapter is ELasLeaderoardAdapter) {
                currentLeaderboardList = it
                (recycler_elasFrag_questions.adapter as ELasLeaderoardAdapter).leaderboardList = it.sortedBy { it.Rank }
                (recycler_elasFrag_questions.adapter as ELasLeaderoardAdapter).notifyDataSetChanged()
            } else if (it.isNotEmpty() && recycler_elasFrag_questions.adapter !is ELasLeaderoardAdapter) {
                currentLeaderboardList = it
            }
        })
    }

    private fun initializeView() {
        val recycler = view!!.findViewById<RecyclerView>(R.id.recycler_elasFrag_questions)
        recycler.adapter = ElasQuestionsAdapter(this)

        activity!!.cart.setOnClickListener{
            this.findNavController().navigate(R.id.action_action_game_to_action_cart)
        }

        activity!!.profile.setOnClickListener{
            this.findNavController().navigate(R.id.action_action_game_to_action_profile)
        }

        activity!!.notifications.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_game_to_notificationFragment)
        }
    }

    private fun selectQuestions() {
        activity!!.bttn_Questions_elas.setTextColor(resources.getColor(R.color.text_color))
        activity!!.bttn_Leaderboard_elas.setTextColor(resources.getColor(R.color.colorGrey))
        try {
            recycler_elasFrag_questions.adapter = ElasQuestionsAdapter(this)
            (recycler_elasFrag_questions.adapter as ElasQuestionsAdapter).questionsList = currentQuestionsList.toSortedMap(reverseOrder())
            (recycler_elasFrag_questions.adapter as ElasQuestionsAdapter).notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("ElasFragment", "Error = ${e.toString()}")
        }
    }

    private fun selectLeaderboard() {
        activity!!.bttn_Leaderboard_elas.setTextColor(resources.getColor(R.color.text_color))
        activity!!.bttn_Questions_elas.setTextColor(resources.getColor(R.color.colorGrey))
        try {
            recycler_elasFrag_questions.adapter = ELasLeaderoardAdapter(context!!)
            (recycler_elasFrag_questions.adapter as ELasLeaderoardAdapter).leaderboardList = currentLeaderboardList.sortedBy { it.Rank }
            (recycler_elasFrag_questions.adapter as ELasLeaderoardAdapter).notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("ElasFragment", "Error = ${e.toString()}")
        }
    }

    override fun onResume() {
        Log.d("ElasFragment", "OnREsume Called")
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_elas)
        activity!!.linearElasRecycler.isVisible = true
        activity!!.search.isVisible = false
        activity!!.textView7.isVisible = false
        activity!!.fragmentName.text = "Quiz"
        activity!!.refresh.isVisible = false
        super.onResume()
    }

    override fun onPause() {
        Log.d("ElasFragment", "OnPause Called")
        // activity!!.linearElasRecycler.isVisible = false
        super.onPause()
    }

    override fun answerQuestion(questionId: Long) {
        val bundle = bundleOf("questionId" to questionId)
        view!!.findNavController().navigate(R.id.action_action_game_to_ELASQuestionFragment, bundle)
    }

    override fun viewRules(questionId: String) {
        Log.d("Elas Fragment", "Recived Category = ${questionId.toString()}")
        val bundle = Bundle()
        if (questionId == "Miscellaneous" || questionId == "null") {
            Toast.makeText(context, "The rules would be announced soon", Toast.LENGTH_LONG).show()
        } else {
            Log.d("ElasFrag", "Applying round = $questionId")
            bundle.apply {
                this.putString("round", questionId)
            }
            DialogRules().apply {
                arguments = bundle
            }.show(childFragmentManager, "RulesDialog")
        }
    }

}
