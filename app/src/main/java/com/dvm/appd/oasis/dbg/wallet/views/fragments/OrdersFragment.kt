package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartChildAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.OrdersAdapter
import com.labo.kaji.fragmentanimations.FlipAnimation
import com.labo.kaji.fragmentanimations.MoveAnimation
import kotlinx.android.synthetic.main.fra_wallet_orders.view.*
import kotlinx.android.synthetic.main.fra_wallet_orders.view.progressBar

class OrdersFragment : Fragment(), OrdersAdapter.OrderCardClick, CartChildAdapter.OnButtonClicked {

    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ordersViewModel = ViewModelProviders.of(this, OrdersViewModelFactory())[OrdersViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_wallet_orders, container, false)

        view.orderRecycler.adapter = OrdersAdapter(this)
        ordersViewModel.orders.observe(this, Observer {

            (view.orderRecycler.adapter as OrdersAdapter).orderItems = it
            (view.orderRecycler.adapter as OrdersAdapter).notifyDataSetChanged()
        })

        view.cartItemRecycler.adapter = CartAdapter(this)
        ordersViewModel.cartItems.observe(this, Observer {

            Log.d("CartPassed", it.toString())
            (view.cartItemRecycler.adapter as CartAdapter).cartItems = it
            (view.cartItemRecycler.adapter as CartAdapter).price = it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.currentPrice }}
            (view.cartItemRecycler.adapter as CartAdapter).notifyDataSetChanged()

            if (it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.currentPrice  }} != 0) {
                view.order.isVisible = true
                view.cartPrice.text = "Total: ₹ ${it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.currentPrice }}}"
            }
            else{
                view.order.isVisible = false
                view.cartPrice.text = ""
            }
            })

        view.order.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Place Order?")
                .setPositiveButton("OK") { _, _ ->
                    (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
                    ordersViewModel.placeOrder()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

        ordersViewModel.progressBarMark.observe(this, Observer {

            if (it == 0){
                view.progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            else if (it == 1){
                view.progressBar.visibility = View.GONE
                view.swipeOrder.isRefreshing = false
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        ordersViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context, ordersViewModel.error.value, Toast.LENGTH_LONG).show()
                (ordersViewModel.error as MutableLiveData).postValue(null)
            }
        })

        view.swipeOrder.setOnRefreshListener {
            (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
            ordersViewModel.refreshData()
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

    override fun plusButtonClicked(item: ModifiedCartData, quantity: Int) {
        ordersViewModel.updateCartItems(item.itemId, quantity)
    }

    override fun deleteCartItemClicked(itemId: Int) {
        ordersViewModel.deleteCartItem(itemId)
    }

    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_orders)
        super.onResume()
    }

    /*override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return MoveAnimation.create(MoveAnimation.RIGHT,true, 500)
    }*/
}
