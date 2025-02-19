package com.example.revive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val cartItems: List<Item>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name_cart)
        val itemDescription: TextView = itemView.findViewById(R.id.item_description_cart)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price_cart)
        val itemImage: ImageView = itemView.findViewById(R.id.item_image_cart)

        fun bind(item: Item) {
            itemName.text = item.name
            itemDescription.text = item.description
            itemPrice.text = context.getString(R.string.price_format, item.price)
            Glide.with(context).load(item.imageUrl).into(itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
