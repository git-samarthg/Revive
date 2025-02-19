package com.example.revive

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val name: String = "",        // Name of the item
    val description: String = "", // Description of the item
    val price: Double = 0.0,      // Price of the item
    val imageUrl: String = "",    // URL of the item image
    val userId: String = "",      // ID of the user who listed the item
    var isSelected: Boolean = false // Track if the item is selected
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte() // Convert byte to boolean (isSelected)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeString(imageUrl)
        parcel.writeString(userId)
        parcel.writeByte(if (isSelected) 1 else 0) // Convert boolean to byte
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
