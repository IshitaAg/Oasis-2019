package com.dvm.appd.oasis.dbg.elas.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.viewModel.RulesDialogViewModel
import com.dvm.appd.oasis.dbg.elas.viewModel.RulesDialogViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dia_rules_elas.*
import kotlinx.android.synthetic.main.dia_rules_elas.view.*

class DialogRules: DialogFragment() {

    lateinit var roundId: String

    private val rulesViewModel by lazy {
        ViewModelProviders.of(this, RulesDialogViewModelFactory())[RulesDialogViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dia_rules_elas, container, false)
        arguments?.let {
            roundId = it.getString("round")!!
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (roundId != "") {
            rulesViewModel.getRulesForRound(roundId)
        } else {
            view.text_rules_dialog_rulesText.text = "Rules to be announced Soon"
        }

        rulesViewModel.error.observe(this, Observer {
            if (it != null) {
                progress_rules_elas.isVisible = true
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
            }
        })

        rulesViewModel.isLoading.observe(this, Observer {
            progress_rules_elas.isVisible = true
            activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })

        rulesViewModel.rules.observe(this, Observer {
            progress_rules_elas.isVisible = false
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            var rules_Text = ""
            for((index, string) in it.withIndex()) {
                rules_Text += "${index + 1}. ${string}\n\n"
            }
            if (rules_Text == "") {
                view.text_rules_dialog_rulesText.text = "Rules to be announced Soon"
            } else {
                view.text_rules_dialog_rulesText.text = rules_Text
            }
            Log.d("Elas Dialog", "Final rules = ${rules_Text}")

        })
    }

    override fun onDetach() {
        super.onDetach()
        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}