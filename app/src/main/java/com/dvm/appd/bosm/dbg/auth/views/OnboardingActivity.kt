package com.dvm.appd.bosm.dbg.auth.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dvm.appd.bosm.dbg.MainActivity
import com.dvm.appd.bosm.dbg.R
import com.dvm.appd.bosm.dbg.di.AppModule
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.ogaclejapan.smarttablayout.SmartTabLayout
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding1.*

class OnboardingActivity : AppCompatActivity(), onboardingFragmentSkipButtonClickListener {

    lateinit var pageAdapter: FragmentStatePagerAdapter
    val NO_OF_PAGES = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        pageAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> {
                        bttn_next_onBoarding.setTextColor(resources.getColor(R.color.trackEvents))
                        return Onboarding1Fragment(
                            this@OnboardingActivity,
                            R.drawable.ic_track_events,
                            "Track Events",
                            resources.getColor(R.color.trackEvents)
                        )
                    }

                    1 -> {
                        bttn_next_onBoarding.setTextColor(resources.getColor(R.color.orderFood))
                        return Onboarding1Fragment(
                            this@OnboardingActivity,
                            R.drawable.ic_online_order,
                            "Order Food",
                            resources.getColor(R.color.orderFood)
                        )
                    }

                    2 -> {
                        bttn_next_onBoarding.setTextColor(resources.getColor(R.color.games))
                        return Onboarding1Fragment(
                            this@OnboardingActivity,
                            R.drawable.ic_games,
                            "Games",
                            resources.getColor(R.color.games)
                        )
                    }

                    3 -> {
                        bttn_next_onBoarding.setTextColor(resources.getColor(R.color.buyTickets))
                        return Onboarding1Fragment(
                            this@OnboardingActivity,
                            R.drawable.ic_buy_tickets,
                            "Buy Tickets",
                            resources.getColor(R.color.buyTickets)
                        )
                    }
                }
                return Onboarding1Fragment(
                    this@OnboardingActivity,
                    R.drawable.ic_track_events,
                    "Track Events",
                    resources.getColor(R.color.trackEvents)
                )

            }

            override fun getCount(): Int {
                return NO_OF_PAGES
            }
        }
        viewPager_onBoarding.adapter = pageAdapter
        bttn_next_onBoarding.setOnClickListener {
            if (viewPager_onBoarding.currentItem == 4) {
                //bttn_next_onBoarding.isClickable = false
                Log.d("checkon","called")
                finishOnBoarding()
            } else {
                viewPager_onBoarding.setCurrentItem(viewPager_onBoarding.currentItem + 1, false)
            }
        }
        viewPager_onBoarding.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("Onboarding Activity", "State Variable = $state")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("Onboarding Activity", "Position1 Variable = $position")
            }

            override fun onPageSelected(position: Int) {
                Log.d("Onboarding Activity", "Position Variable = $position")
                if (position == (NO_OF_PAGES - 1)) {
                    Log.d("Onboarding Activity", "Entered if condition")
                    this@OnboardingActivity.bttn_next_onBoarding.text = "Done"
                } else {
                    this@OnboardingActivity.bttn_next_onBoarding.text = "Next"
                }
            }

        })
    }

    override fun onSkipButtonPressed() {
        finishOnBoarding()
    }

    private fun finishOnBoarding() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
