package com.example.listviewapp.views

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listviewapp.R
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.listviewapp.model.Animal
import com.example.listviewapp.viewmodels.ListViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up the "Add" button
        val addButton: Button = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            // Show the Add Animal dialog
            showAddAnimalDialog()
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

    // Function to show the Add Animal dialog
    private fun showAddAnimalDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_animal, null)
        builder.setView(dialogView)

        // Find views from the dialog layout
        val editText = dialogView.findViewById<EditText>(R.id.addAnimalName)
        val saveButton = dialogView.findViewById<Button>(R.id.addSaveButton)

        val dialog = builder.create()

        // Set up the Save button click listener
        saveButton.setOnClickListener {
            val animalName = editText.text.toString().trim()
            if (animalName.isNotEmpty()) {
                val randomId = (1..1000).random() // Generate a random ID
                val newAnimal = Animal(randomId, animalName)

                viewModel.addAnimal(newAnimal) // Add the new animal through ViewModel
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}