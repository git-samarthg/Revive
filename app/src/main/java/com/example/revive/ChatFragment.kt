// ChatFragment.kt
package com.example.revive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revive.databinding.FragmentChatBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.revive.utils.ToastHelper

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<String>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(requireContext(), chatMessages)
        binding.recyclerViewChat.adapter = chatAdapter

        // Load chat messages from Firestore
        loadChatMessages()

        // Send a test message when the button is clicked

    }

    // Save the toast message with the current timestamp to Firestore
    private fun saveToastMessageToFirestore(message: String) {
        val toastMessage = hashMapOf(
            "message" to message,
            "timestamp" to Timestamp.now()
        )

        firestore.collection("chatMessages").add(toastMessage)
            .addOnSuccessListener {
                ToastHelper.showToastAndLog(requireContext(), "Message saved to Firestore") // Modified here
                loadChatMessages() // Reload messages after saving
            }
            .addOnFailureListener { e ->
                ToastHelper.showToastAndLog(requireContext(), "Failed to save message") // Modified here
                e.printStackTrace()
            }
    }

    // Load chat messages from Firestore
    private fun loadChatMessages() {
        firestore.collection("chatMessages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { documents ->
                chatMessages.clear() // Clear the list before loading new messages
                for (document in documents) {
                    val message = document.getString("message") ?: ""
                    val timestamp = document.getTimestamp("timestamp")
                    val timeFormatted = timestamp?.toDate()?.toString() ?: "Unknown time"
                    val messageWithTime = "$message (Sent at: $timeFormatted)"
                    chatMessages.add(messageWithTime)
                }
                chatAdapter.notifyDataSetChanged() // Notify the adapter of the data change
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace() // Handle any errors
            }
    }
}
