package com.dvm.appd.oasis.dbg.wallet.views.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import com.dvm.appd.oasis.dbg.wallet.viewmodel.CartViewModel
import com.dvm.appd.oasis.dbg.wallet.viewmodel.CartViewModelFactory
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartAdapter
import com.dvm.appd.oasis.dbg.wallet.views.adapters.CartChildAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_cart.view.*

class CartFragment: Fragment(), CartChildAdapter.OnButtonClicked {

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fra_cart, container, false)

        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_cart)
        activity!!.search.isVisible = false
        activity!!.textView7.isVisible = false
        activity!!.refresh.isVisible = false

        cartViewModel = ViewModelProviders.of(this, CartViewModelFactory())[CartViewModel::class.java]

        view.cartRecycler.adapter = CartAdapter(this)

        cartViewModel.cartItems.observe(this, Observer {

            (view.cartRecycler.adapter as CartAdapter).cartItems = it
            (view.cartRecycler.adapter as CartAdapter).notifyDataSetChanged()

            if (it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.price  }} != 0) {
                view.cartOrderView.isVisible = true
                view.totalPrice.text = "Total: ₹ ${it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity * it2.price }}}"
                view.itemCount.text = "${it.sumBy { it1 -> it1.second.sumBy {it2 ->  it2.quantity} }} items"
            } else {
                view.totalPrice.text = ""
                view.itemCount.text = ""
                view.placeOrder.text = ""
                view.cartOrderView.isVisible = false
            }
        })

        view.cartOrderView.setOnClickListener {
            (cartViewModel.progressBarMark as MutableLiveData).postValue(0)
            cartViewModel.placeOrder()
        }

        cartViewModel.error.observe(this, Observer {
            if (it != null) {
                Toast.makeText(context, cartViewModel.error.value, Toast.LENGTH_LONG).show()
                (cartViewModel.error as MutableLiveData).postValue(null)
            }
        })

        cartViewModel.progressBarMark.observe(this, Observer {

            if (it == 0) {
                view.progressBar.visibility = View.VISIBLE
                view.cartOrderView.isClickable = false
            }
            else if (it == 1) {
                view.progressBar.visibility = View.GONE
                view.cartOrderView.isClickable = true
            }
        })

//        cartViewModel.redirect.observe(this, Observer {
//
//            if(it)
//                view.findNavController().navigate(R.id.action_action_cart_to_orders)
//
//        })

        view.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        return view
    }

    override fun plusButtonClicked(item: ModifiedCartData, quantity: Int) {
        cartViewModel.updateCartItems(item.itemId, quantity)
    }

    override fun deleteCartItemClicked(itemId: Int) {
        cartViewModel.deleteCartItem(itemId)
    }
}
