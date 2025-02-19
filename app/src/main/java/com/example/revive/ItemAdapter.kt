package com.example.revive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(
    private val context: Context,
    private val itemList: List<Item>,
    private val itemClickListener: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val selectedItems = mutableSetOf<Item>() // Store selected items

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemDescription: TextView = itemView.findViewById(R.id.item_description)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val cardView: View = itemView.findViewById(R.id.item_card_view) // For animating

        fun bind(item: Item) {
            itemName.text = item.name
            itemDescription.text = item.description
            itemPrice.text = context.getString(R.string.price_format, item.price)

            // Load image using Glide
            Glide.with(context)
                .load(item.imageUrl)
                .into(itemImage)

            // Change appearance based on selection state
            if (selectedItems.contains(item)) {
                applySelectedAnimation(cardView) // Animate the item when selected
            } else {
                applyDeselectedAnimation(cardView) // Reset animation when deselected
            }

            // Handle item click
            itemView.setOnClickListener {
                toggleSelection(item)
                itemClickListener(item)
            }
        }

        // Apply scale animation when the item is selected
        private fun applySelectedAnimation(view: View) {
            val animation = ScaleAnimation(
                1f, 1.1f, // Start and end scale for X axis
                1f, 1.1f, // Start and end scale for Y axis
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y (center)
            )
            animation.duration = 300 // Duration of the animation
            animation.fillAfter = true // Keep the final scale state
            view.startAnimation(animation)
        }

        // Reset animation when the item is deselected
        private fun applyDeselectedAnimation(view: View) {
            val animation = ScaleAnimation(
                1.1f, 1f, // Reverse scale for X axis
                1.1f, 1f, // Reverse scale for Y axis
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            animation.duration = 300
            animation.fillAfter = true
            view.startAnimation(animation)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Toggle selection state and update animation
    private fun toggleSelection(item: Item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item) // Deselect item
        } else {
            selectedItems.add(item) // Select item
        }
        notifyDataSetChanged() // Refresh the adapter to update animations
    }
}
