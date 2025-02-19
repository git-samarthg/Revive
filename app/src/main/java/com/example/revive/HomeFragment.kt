package com.example.revive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize buttons and set click listeners
        val button1 = view.findViewById<ImageButton>(R.id.button1)
        val button2 = view.findViewById<ImageButton>(R.id.button2)
        val button3 = view.findViewById<ImageButton>(R.id.button3)
        val button4 = view.findViewById<ImageButton>(R.id.button4)

        button1.setOnClickListener {
            // Start Activity 1
            val intent = Intent(activity, Activity1::class.java) // Replace with your actual activity
            startActivity(intent)
        }

        button2.setOnClickListener {
            // Start Activity 2
            val intent = Intent(activity, Activity2::class.java) // Replace with your actual activity
            startActivity(intent)
        }

        button3.setOnClickListener {
            // Start Activity 3
            val intent = Intent(activity, Activity3::class.java) // Replace with your actual activity
            startActivity(intent)
        }

        button4.setOnClickListener {
            // Start Activity 4
            val intent = Intent(activity, Activity4::class.java) // Replace with your actual activity
            startActivity(intent)
        }

        return view
    }
}
