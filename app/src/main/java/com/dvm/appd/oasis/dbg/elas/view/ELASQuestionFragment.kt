package com.dvm.appd.oasis.dbg.elas.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity

import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.UIStateElas
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.dvm.appd.oasis.dbg.elas.view.adapter.ElasOptionsAdapter
import com.dvm.appd.oasis.dbg.elas.viewModel.ElasQuestionViewModel
import com.dvm.appd.oasis.dbg.elas.viewModel.ElasQuestionViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_elasquestion.*
import java.util.concurrent.TimeUnit

class ELASQuestionFragment : Fragment(), ElasOptionsAdapter.OnOptionSelected {

    var questionId: Long = 0
    val elasQuestionViewModel: ElasQuestionViewModel by lazy {
        ViewModelProviders.of(this, ElasQuestionViewModelFactory())[ElasQuestionViewModel::class.java]
    }
    var selectedOptionId: Long = -1
    var currentOptionsList = emptyList<CombinedQuestionOptionDataClass>()
    var currentSnackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        // Inflate the layout for this fragment
        questionId = arguments?.getLong("questionId")!!
        return inflater.inflate(R.layout.fragment_elasquestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_card_elasFrag_question.movementMethod = ScrollingMovementMethod()

        if (questionId != 0.toLong()) {
            elasQuestionViewModel.getQuestion(questionId)
        } else {
            currentSnackbar = Snackbar.make(activity!!.coordinator_parent, "Please Select a Valid Question", Snackbar.LENGTH_SHORT)
            currentSnackbar!!.show()
        }

        recycler_elasOptionsFrag_options.adapter = ElasOptionsAdapter(this)

        elasQuestionViewModel.uiState.observe(this, Observer {
            when(it) {
                is UIStateElas.Failure -> {
                    progressBar.visibility = View.INVISIBLE
                    activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    currentSnackbar = Snackbar.make(activity!!.coordinator_parent, (it as UIStateElas.Failure).message, Snackbar.LENGTH_SHORT)
                    currentSnackbar!!.show()
                }
                is UIStateElas.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                is UIStateElas.Questions -> {
                    progressBar.visibility = View.INVISIBLE
                    activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        })

        elasQuestionViewModel.question.observe(this, Observer {
            if (it.isNotEmpty()) {
                text_card_elasFrag_questionNo.text = "Question ${it.first().questionNumber}"
                constraint_question.visibility = View.VISIBLE
                text_card_elasFrag_question.text = it.first().question
                currentOptionsList = it
                (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).optionsList = it
                (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).notifyDataSetChanged()
            }
        })

        backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }

        RxView.clicks(bttn_elasfraQuestions_submitAnswer).debounce(500, TimeUnit.MILLISECONDS).subscribe {
            if (selectedOptionId == (-1).toLong()) {
                Toast.makeText(view.context, "Please select an option", Toast.LENGTH_LONG).show()
            } else {
                elasQuestionViewModel.submitAnswer(questionId, selectedOptionId)
            }
        }

    }

    override fun optionSelected(position: Long) {
        selectedOptionId = position
        (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).optionsList = currentOptionsList
        (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).notifyDataSetChanged()
    }

    override fun noOptionSelected() {
        selectedOptionId = -1
        (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).optionsList = currentOptionsList
        (recycler_elasOptionsFrag_options.adapter as ElasOptionsAdapter).notifyDataSetChanged()
    }

}
