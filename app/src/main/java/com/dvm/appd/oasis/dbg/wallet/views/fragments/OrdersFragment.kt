package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.OrdersAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_wallet_orders.view.*

class OrdersFragment : Fragment(), OrdersAdapter.OrderCardClick {

    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ordersViewModel = ViewModelProviders.of(this, OrdersViewModelFactory())[OrdersViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_wallet_orders, container, false)

        activity!!.mainView.setBackgroundResource(R.drawable.orders_title)


        activity!!.cart.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_order_history_to_action_cart)
        }

        activity!!.profile.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_order_history_to_action_profile)
        }

        activity!!.notifications.setOnClickListener {
            this.findNavController().navigate(R.id.action_action_order_history_to_notificationFragment)
        }
        view.orderRecycler.adapter = OrdersAdapter(this)

        ordersViewModel.orders.observe(this, Observer {

            (view.orderRecycler.adapter as OrdersAdapter).orderItems = it
            (view.orderRecycler.adapter as OrdersAdapter).notifyDataSetChanged()
        })

        ordersViewModel.progressBarMark.observe(this, Observer {

            if (it == 0){
                view.progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            else if (it == 1){
                view.progressBar.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        })

        ordersViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context, ordersViewModel.error.value, Toast.LENGTH_LONG).show()
                (ordersViewModel.error as MutableLiveData).postValue(null)
            }
        })

        activity!!.refresh.setOnClickListener {
            (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
            ordersViewModel.getAllOrders()
        }

        return view
    }

    override fun updateOtpSeen(orderId: Int) {
        (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
        ordersViewModel.updateOtpSeen(orderId)
    }

    override fun showOrderItemDialog(orderId: Int) {
        val bundle = bundleOf("orderId" to orderId)
        OrderItemsDialog().apply { arguments = bundle }.show(childFragmentManager, "OrderItemDialog")
    }

    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_orders)
        activity!!.fragmentName.text = "Orders"
        activity!!.search.isVisible = false
        activity!!.textView7.isVisible = true
        activity!!.textView7.text = "\"Don't forget to add ratings\""
        activity!!.textView7.setTextColor(Color.rgb(28, 140, 204))
        activity!!.linearElasRecycler.isVisible = false
        activity!!.refresh.isVisible = true
        super.onResume()
    }

}
