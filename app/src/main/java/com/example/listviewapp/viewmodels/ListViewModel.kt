package com.example.listviewapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listviewapp.model.Animal
import com.example.listviewapp.network.ApiService

class ListViewModel : ViewModel() {

    // LiveData to hold the list of animals
    private val _animalList = MutableLiveData<List<Animal>?>()
    val animalList: LiveData<List<Animal>?> get() = _animalList

    // LiveData to indicate if there was an error while fetching data
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Function to fetch animals from the API
    fun fetchAnimals() {
        ApiService.getAnimals { animals ->
            if (!animals.isNullOrEmpty()) {
                Log.d("ListViewModel", "Fetched animals: $animals") // Add this
                _animalList.postValue(animals)
            } else {
                _animalList.postValue(emptyList())
                _error.postValue("No animals found")
            }
        }
    }

    // Function to delete an animal from the list
    fun deleteAnimal(animalId: Int) {
        ApiService.deleteAnimal(animalId) { isSuccess ->
            if (isSuccess) {
                // After successful deletion, remove the animal from the list manually
                _animalList.value?.let { currentList ->
                    // Filter out the deleted animal by ID
                    val updatedList = currentList.filter { it.id != animalId }
                    // Post the updated list back to LiveData
                    _animalList.postValue(updatedList)

                    // Optionally, you can check if the list is empty and show a message
                    if (updatedList.isEmpty()) {
                        _error.postValue("No animals left")
                    }
                }
            } else {
                _error.postValue("Failed to delete animal.")
            }
        }
    }

    fun updateAnimal(id: Int, newName: String) {
        ApiService.updateAnimal(id, newName) { isSuccess ->
            if (isSuccess) {
                fetchAnimals() // Reload the list to reflect the updated name
            } else {
                _error.postValue("Failed to update animal.")
            }
        }
    }

    fun addAnimal(animal: Animal) {
        ApiService.addAnimal(animal) { isSuccess ->
            if (isSuccess) {
                Log.d("ListViewModel", "Animal added successfully.") // Add this
                fetchAnimals() // Ensure this is executed after success
            } else {
                Log.e("ListViewModel", "Failed to add animal.")
                _error.postValue("Failed to add animal.")
            }
        }
    }

}

