package com.example.listviewapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.listviewapp.R
import com.example.listviewapp.model.Animal

class ListAdapter(context: Context, private val animals: List<Animal>, private val onDeleteClicked: (Animal) -> Unit) :
    ArrayAdapter<Animal>(context, R.layout.item_list, animals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)

        val textView = view.findViewById<TextView>(R.id.itemText)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        val animal = animals[position]
        textView.text = animal.name // Set the name of the animal in the TextView

        // Set up the delete button to call the delete function
        deleteButton.setOnClickListener {
            // Call the onDeleteClicked callback when the button is clicked
            onDeleteClicked(animal)
        }

        return view
    }
}
