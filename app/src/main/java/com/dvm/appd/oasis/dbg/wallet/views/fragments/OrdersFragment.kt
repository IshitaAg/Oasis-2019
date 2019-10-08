package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.graphics.Color
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import com.dvm.appd.oasis.dbg.wallet.viewmodel.CartViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.CartViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.OrdersViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartChildAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.OrdersAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_cart.view.*
import kotlinx.android.synthetic.main.fra_wallet_orders.view.*
import kotlinx.android.synthetic.main.fra_wallet_orders.view.cartRecycler
import kotlinx.android.synthetic.main.fra_wallet_orders.view.progressBar

class OrdersFragment : Fragment(), OrdersAdapter.OrderCardClick, CartChildAdapter.OnButtonClicked, CartAdapter.OrderButtonClicked {

    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ordersViewModel = ViewModelProviders.of(this, OrdersViewModelFactory())[OrdersViewModel::class.java]

        val view = inflater.inflate(R.layout.fra_wallet_orders, container, false)

        view.orderRecycler.adapter = OrdersAdapter(this)
        ordersViewModel.orders.observe(this, Observer {

            (view.orderRecycler.adapter as OrdersAdapter).orderItems = it
            (view.orderRecycler.adapter as OrdersAdapter).notifyDataSetChanged()
        })

        view.cartRecycler.adapter = CartAdapter(this, this)
        ordersViewModel.cartItems.observe(this, Observer {

            Log.d("Cart", it.toString())
            (view.cartRecycler.adapter as CartAdapter).cartItems = it
            (view.cartRecycler.adapter as CartAdapter).price = it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.price }}
            (view.cartRecycler.adapter as CartAdapter).notifyDataSetChanged()

//            if (it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.price  }} != 0) {
//                view.order.isVisible = true
//                view.cartPrice.text = "Total: ₹ ${it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.price }}}"
//            }
//            else{
//                view.order.isVisible = false
//                view.cartPrice.text = ""
//            }
            })

//        view.order.setOnClickListener {
//            (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
//            ordersViewModel.placeOrder()
//        }

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

    override fun placeOrder() {
        (ordersViewModel.progressBarMark as MutableLiveData).postValue(0)
        ordersViewModel.placeOrder()
    }

    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_orders)
        super.onResume()
    }
}
