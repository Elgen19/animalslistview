package com.example.listviewapp.views

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listviewapp.R
import androidx.fragment.app.FragmentTransaction
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up the "Add" button
        val addButton: Button = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            // Handle Add button click here
            // For example, show a Toast or navigate to another activity
        }

        // Add the ListFragment to the activity
        if (savedInstanceState == null) {
            val fragment = ListFragment()  // Create an instance of ListFragment
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)  // Replace the fragment container with ListFragment
            transaction.commit()  // Commit the transaction
        }

        // Set up Window Insets listener (if you need edge-to-edge support)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}