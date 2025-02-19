// CartFragment.kt
package com.example.revive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revive.utils.ToastHelper // Import the ToastHelper

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<Item>() // To store items in the cart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_cart)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load items from CartManager
        cartItems.addAll(CartManager.selectedProducts) // Fetch selected items

        // Initialize the adapter and set it to the RecyclerView
        cartAdapter = CartAdapter(requireContext(), cartItems)
        recyclerView.adapter = cartAdapter

        // Set up the "Purchase" button
        val purchaseButton = view.findViewById<Button>(R.id.purchase_button)
        purchaseButton.setOnClickListener {
            handlePurchase()
        }

        // Set up the "Barter" button
        val barterButton = view.findViewById<Button>(R.id.barter_button)
        barterButton.setOnClickListener {
            handleBarter()
        }

        // Set up the "Clear Cart" button
        val clearCartButton = view.findViewById<Button>(R.id.clear_cart_button)
        clearCartButton.setOnClickListener {
            clearCart()
        }

        return view
    }

    // Handle Purchase Logic
    private fun handlePurchase() {
        if (cartItems.isNotEmpty()) {
            // Implement purchase logic here
            ToastHelper.showToastAndLog(requireContext(), "Purchase initiated!")
            // You can add more detailed logic like navigating to a purchase screen or performing payment steps
        } else {
            ToastHelper.showToastAndLog(requireContext(), "Cart is empty!")
        }
    }

    // Handle Barter Logic
    private fun handleBarter() {
        if (cartItems.isNotEmpty()) {
            // Implement barter logic here
            ToastHelper.showToastAndLog(requireContext(), "Barter process started!")
            // You can add logic to start a barter exchange process
        } else {
            ToastHelper.showToastAndLog(requireContext(), "Cart is empty!")
        }
    }

    // Clear the cart
    private fun clearCart() {
        cartItems.clear() // Clear the items from the cart
        cartAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
        ToastHelper.showToastAndLog(requireContext(), "Cart cleared!") // Show a toast message
    }
}
