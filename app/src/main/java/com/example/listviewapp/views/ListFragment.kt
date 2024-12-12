package com.example.listviewapp.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listviewapp.R
import com.example.listviewapp.adapters.ListAdapter
import com.example.listviewapp.model.Animal
import com.example.listviewapp.viewmodels.ListViewModel

class ListFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        // Initialize the ListView
        listView = rootView.findViewById(R.id.listView)

        // Observe changes to the animal list
        // Observe changes to the animal list
        viewModel.animalList.observe(viewLifecycleOwner, Observer { animals ->
            if (animals != null && animals.isNotEmpty()) {
                Log.d("ListFragment", "Animal list updated: $animals")

                // Update the adapter or reinitialize it if necessary
                listAdapter = ListAdapter(
                    requireContext(),
                    animals, // The new list of animals
                    onDeleteClicked = { animal -> viewModel.deleteAnimal(animal.id) },
                    onEditClicked = { animal -> showEditDialog(animal) }
                )
                listView.adapter = listAdapter

                // Optionally, notify the adapter of the change
                listAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "No animals found", Toast.LENGTH_SHORT).show()
            }
        })

        // Observe errors (optional)
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                // Show error message (e.g., using a Toast)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch the animals when the fragment is created
        viewModel.fetchAnimals()

        return rootView
    }

    // Function to show the edit dialog
    private fun showEditDialog(animal: Animal) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update_animal, null)
        builder.setView(dialogView)

        // Find views from the dialog layout
        val editText = dialogView.findViewById<EditText>(R.id.editAnimalName)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        // Pre-fill the EditText with the current animal name
        editText.setText(animal.name)

        val dialog = builder.create()

        // Set up the Save button click listener
        saveButton.setOnClickListener {
            val updatedName = editText.text.toString().trim()
            if (updatedName.isNotEmpty()) {
                // Call ViewModel to update the animal
                viewModel.updateAnimal(animal.id, updatedName)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

}
