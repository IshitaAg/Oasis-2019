package com.dvm.appd.oasis.dbg.auth.views

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.NetworkChangeNotifier
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModel
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModelFactory
import com.dvm.appd.oasis.dbg.shared.NetworkChangeReciver
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.password
import kotlinx.android.synthetic.main.activity_auth.username
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_picture.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class AuthActivity : AppCompatActivity(),NetworkChangeNotifier {
    private lateinit var authViewModel: AuthViewModel
    private var code: String = ""
    private var receiver: NetworkChangeReciver? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var rootView:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(R.color.OnBoarding_colour)
        // Hide the status bar.
       // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
       // actionBar?.hide()
        setContentView(R.layout.activity_auth)

        val gso = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1005380465971-dku54t11d22mnk06pkcs6jjilnjfe2sd.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build()
        )
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetworkChangeReciver(this)
        this.registerReceiver(receiver, filter)
        authViewModel = ViewModelProviders.of(this, AuthViewModelFactory())[AuthViewModel::class.java]

        RxView.clicks(outsteeLogin).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when {
                username.text.toString().isBlank() || password.text.toString().isBlank() ->
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                else -> {
                    code = authViewModel.authRepository.sharedPreferences.getString(AuthRepository.Keys.referredBy, "")!!
                    authViewModel.login(username.text.toString(), password.text.toString(),code)
                    outsteeLogin.setBackgroundColor(Color.parseColor("#00000000"))
                    showLoadingState()
                    CircularLoadingButton.startAnimation()
                    authViewModel.login(username.text.toString(),password.text.toString(), code)
                }
            }
        }
        RxView.clicks(bitsianLogin).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            gso.signOut().addOnCompleteListener {
                startActivityForResult(gso.signInIntent, 108)
            }
        }

        authViewModel.state.observe(this, Observer {
            when (it!!) {
                LoginState.MoveToMainApp -> {
                    removeLoadingState()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                LoginState.MoveToOnBoarding -> {
                    removeLoadingState()
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    finish()
                }
                is LoginState.Failure -> {

                    outsteeLogin.background = resources.getDrawable(R.drawable.add_button_profile)
                    CircularLoadingButton.revertAnimation()
                    circularLoadingButtonBitsMail.revertAnimation()
                    removeLoadingState()
                    Toast.makeText(this, (it as LoginState.Failure).message, Toast.LENGTH_LONG)
                        .show()
                }
                LoginState.MoveToPic -> {
                    removeLoadingState()
                    startActivity(Intent(this,PictureActivity::class.java))

                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.OnBoarding_colour)
/*
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_auth))
*/
        // Hide the status bar.
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
       // actionBar?.hide()
        authViewModel.referralState.observe(this, Observer {
            if (it){
                ReferralEnterDialog().show(supportFragmentManager, "ReferralStart")
            }
        })
    }

//    override fun onStart() {
//        super.onStart()
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 108) {
            try {
                val profile = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
                circularLoadingButtonBitsMail.startAnimation()
                showLoadingState()
                // Toast.makeText(this, profile!!.displayName, Toast.LENGTH_SHORT).show()
                authViewModel.Blogin(profile!!.idToken!!, code)
            } catch (e: ApiException) {
                Log.d("checke", e.toString())
                circularLoadingButtonBitsMail.revertAnimation()
                removeLoadingState()
                Toast.makeText(this, "{${e.statusCode}: Sign in Failure!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        this.unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press Once More to Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun setStatusBarColor(color: Int) {
        Log.d("MainActivity", "Entered function to change color")
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,color))
    }

    private fun showLoadingState(){
           window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }

    private fun removeLoadingState(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setStatusBarColor(R.color.OnBoarding_colour)
        // Hide the status bar.
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
       // actionBar?.hide()
    }
    override fun onNetworkStatusScahnged(isConnected: Boolean) {
        Log.d("check","called")
        if (isConnected) {
            outsteeLogin.isEnabled=true
            bitsianLogin.isEnabled =true
            val snackbar = Snackbar.make(this.coordinator_auth, "Back Online", Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(resources.getColor(R.color.colorGreen))
            snackbar.show()
        } else {
            outsteeLogin.isEnabled = false
            bitsianLogin.isEnabled = false
            Snackbar.make(this.coordinator_auth, "Not Connected to the internet", Snackbar.LENGTH_INDEFINITE).setBehavior(object : BaseTransientBottomBar.Behavior(){
                override fun canSwipeDismissView(child: View): Boolean {
                    return false
                }
            }).show()
        }
    }

}
