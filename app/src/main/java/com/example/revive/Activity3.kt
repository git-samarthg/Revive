// Activity3.kt
package com.example.revive

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revive.utils.ToastHelper // Import the ToastHelper

class Activity3 : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var amountEditText: EditText
    private lateinit var splitButton: Button
    private lateinit var selectedUsersTextView: TextView

    private val userList = listOf(
        User("Samarth"),
        User("Rakesh"),
        User("Aalok"),
        User("Sohum")
    )

    private val selectedUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity3)

        userRecyclerView = findViewById(R.id.user_recycler_view)
        amountEditText = findViewById(R.id.amount_edit_text)
        splitButton = findViewById(R.id.split_button)
        selectedUsersTextView = findViewById(R.id.selected_users_text_view)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter(userList) { user, isSelected ->
            if (isSelected) {
                selectedUsers.add(user)
            } else {
                selectedUsers.remove(user)
            }
            updateSelectedUsersText()
        }
        userRecyclerView.adapter = adapter

        splitButton.setOnClickListener {
            val amountText = amountEditText.text.toString()
            if (amountText.isNotEmpty() && selectedUsers.isNotEmpty()) {
                val amount = amountText.toDouble()
                val splitAmount = amount / selectedUsers.size
                ToastHelper.showToastAndLog(this, "Payment will be initiated when all users send \$${splitAmount}")
            } else {
                ToastHelper.showToastAndLog(this, "Please enter an amount and select users.")
            }
        }
    }

    private fun updateSelectedUsersText() {
        selectedUsersTextView.text = "Selected Users: ${selectedUsers.joinToString(", ") { it.name }}"
    }
}
