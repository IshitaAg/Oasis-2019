package com.dvm.appd.oasis.dbg.auth.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dvm.appd.oasis.dbg.R
import com.google.android.play.core.internal.x
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.fragment_onboarding1.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

interface onboardingFragmentButtonClickListener {
    fun onSkipButtonPressed()
    fun onNextButtonClicked()
}

class Onboarding1Fragment(val listener: onboardingFragmentButtonClickListener, val image: Int, val heading: String, val background:Int, val body: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding1, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        img_onBoarding.setImageDrawable(resources.getDrawable(image))
        text_onBoarding_heading.text = heading
        parent.setBackgroundColor(background)
        text_onBoarding_content.text = body
        RxView.clicks(text_bttn_skip).debounce(200, TimeUnit.MILLISECONDS).subscribe {
            text_bttn_skip.isClickable = false
            listener.onSkipButtonPressed()
        }
        RxView.clicks(bttn_next_onBoarding).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            bttn_next_onBoarding.isClickable = false
            listener.onNextButtonClicked()
        }
        super.onViewCreated(view, savedInstanceState)
    }


}
