// SellFragment.kt
package com.example.revive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revive.utils.ToastHelper // Import the ToastHelper
import com.google.firebase.firestore.FirebaseFirestore

class SellFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = mutableListOf<Item>() // To store fetched items from Firestore
    private val selectedProducts = mutableListOf<Item>() // Store selected items for cart
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sell, container, false)

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            toggleSelection(selectedItem)
        }
        recyclerView.adapter = itemAdapter

        // Set up the "Add Item" button
        val addItemButton = view.findViewById<Button>(R.id.add_item_button)
        addItemButton.setOnClickListener {
            val intent = Intent(activity, AddItemActivity::class.java)
            startActivity(intent)
        }

        // Set up the "Add to Cart" button
        val addToCartButton = view.findViewById<Button>(R.id.add_to_cart_button)
        addToCartButton.setOnClickListener {
            if (selectedProducts.isNotEmpty()) {
                // Add selected products to CartManager
                CartManager.selectedProducts.addAll(selectedProducts)

                // Show a Toast message indicating items were added
                ToastHelper.showToastAndLog(requireContext(), "Items added to cart successfully!")

                // Clear selection if needed
                selectedProducts.clear()
                itemAdapter.notifyDataSetChanged() // Refresh the adapter
            } else {
                ToastHelper.showToastAndLog(requireContext(), "No items selected to add to cart.")
            }
        }

        // Load and display items listed for selling
        loadListedItems()

        return view
    }

    private fun loadListedItems() {
        // Fetch items from Firestore
        firestore.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear() // Clear the list before adding new items
                for (document in documents) {
                    val item = document.toObject(Item::class.java)
                    itemList.add(item) // Add the item to the list
                }
                itemAdapter.notifyDataSetChanged() // Notify the adapter of the data change
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                exception.printStackTrace()
            }
    }

    private fun toggleSelection(item: Item) {
        if (selectedProducts.contains(item)) {
            selectedProducts.remove(item) // Deselect if already selected
        } else {
            selectedProducts.add(item) // Select the item
        }
        itemAdapter.notifyDataSetChanged() // Notify the adapter of the data change
    }
}
