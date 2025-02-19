package com.example.revive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.revive.utils.ToastHelper


class ProfileFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var welcomeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var userDataTextView: TextView

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var userDataEditText: EditText

    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        userDataTextView = view.findViewById(R.id.userDataTextView)

        nameEditText = view.findViewById(R.id.nameEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        genderEditText = view.findViewById(R.id.genderEditText)
        locationEditText = view.findViewById(R.id.locationEditText)
        userDataEditText = view.findViewById(R.id.userDataEditText)

        addButton = view.findViewById(R.id.addButton)

        // Fetch user details from Firestore
        loadUserData()

        // Set OnClickListener to add or update user data
        addButton.setOnClickListener {
            addOrUpdateUserData()
        }

        return view
    }

    // Function to load user data from Firestore
    private fun loadUserData() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: ""
                        val age = document.getLong("age")?.toInt() ?: 0
                        val gender = document.getString("gender") ?: ""
                        val location = document.getString("location") ?: ""
                        val userData = document.getString("userData") ?: ""

                        // Set data to views
                        welcomeTextView.text = "Welcome $name"
                        emailTextView.text = "Email: ${currentUser.email}"
                        userDataTextView.text = "Let's start reviving!\n\n$userData"

                        // Populate edit texts for possible updates
                        nameEditText.setText(name)
                        ageEditText.setText(age.toString())
                        genderEditText.setText(gender)
                        locationEditText.setText(location)
                        userDataEditText.setText(userData)
                    } else {
                        ToastHelper.showToastAndLog(requireContext(), "No data found for user") // Modified here
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileFragment", "Error fetching user data", e)
                    ToastHelper.showToastAndLog(requireContext(), "Error loading data") // Modified here
                }
        }
    }

    // Function to add or update user data in Firestore
    private fun addOrUpdateUserData() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull() ?: 0
            val gender = genderEditText.text.toString()
            val location = locationEditText.text.toString()
            val userData = userDataEditText.text.toString()

            // Data to be stored or updated in Firestore
            val user = hashMapOf(
                "name" to name,
                "age" to age,
                "gender" to gender,
                "location" to location,
                "userData" to userData
            )

            // Add or update user data in Firestore
            db.collection("users").document(userId).set(user)
                .addOnSuccessListener {
                    ToastHelper.showToastAndLog(requireContext(), "Data saved successfully!") // Modified here
                    // Update the UI with the latest data
                    loadUserData()
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileFragment", "Error saving user data", e)
                    ToastHelper.showToastAndLog(requireContext(), "Failed to save data") // Modified here
                }
        }
    }
}
