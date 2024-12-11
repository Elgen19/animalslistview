package com.example.listviewapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listviewapp.R
import com.example.listviewapp.adapters.ListAdapter
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
        viewModel.animalList.observe(viewLifecycleOwner, Observer { animals ->
            if (animals != null && animals.isNotEmpty()) {
                // Initialize the adapter with the list of animals and the delete callback
                listAdapter = ListAdapter(requireContext(), animals) { animal ->
                    // When delete button is clicked, call deleteAnimal
                    viewModel.deleteAnimal(animal.id)
                }
                listView.adapter = listAdapter
            } else {
                // Handle empty list or null response (e.g., show a message to the user)
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
}
