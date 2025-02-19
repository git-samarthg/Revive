package com.example.revive

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Activity2 : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        // Set max value for progress bar (15 as the limit)
        progressBar.max = 15

        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Fetch the number of users from Firestore
        db.collection("users").get()
            .addOnSuccessListener { querySnapshot ->
                val userCount = querySnapshot.size() // Get the count of users
                animateProgress(userCount) // Animate progress bar update
            }
            .addOnFailureListener {
                // Handle failure
                progressText.text = "Failed to load user count"
            }
    }

    // Function to animate progress bar and update text
    private fun animateProgress(userCount: Int) {
        // Animate progress bar to smoothly transition to the user count
        ObjectAnimator.ofInt(progressBar, "progress", userCount).apply {
            duration = 1000 // 1 second animation duration
            start()
        }

        // Update the progress text
        progressText.text = "Keep transacting to earn a badge"

        if (userCount >= 15) {
            // Optional: Add a message or change UI when progress reaches max
            progressText.text = "Platform goal reached! $userCount users."
        }
    }
}
