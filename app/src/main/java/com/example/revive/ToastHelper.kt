package com.example.revive.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.util.HashMap

object ToastHelper {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to display the toast and store the message in Firestore
    fun showToastAndLog(context: Context, message: String) {
        // Display the toast message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        // Create a chat message object to store in Firestore
        val chatMessage = HashMap<String, Any>()
        chatMessage["message"] = message
        chatMessage["timestamp"] = FieldValue.serverTimestamp()

        // Save the message to Firestore under the 'chatMessages' collection
        firestore.collection("chatMessages")
            .add(chatMessage)
            .addOnSuccessListener {
                // Optionally, log success or handle further actions
            }
            .addOnFailureListener {
                // Handle any errors that occur while saving the message
                it.printStackTrace()
            }
    }
}
