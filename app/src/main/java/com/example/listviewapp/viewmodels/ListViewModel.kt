package com.example.listviewapp.viewmodels

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
            if (animals != null && animals.isNotEmpty()) {
                // Update the LiveData with the fetched list of animals
                _animalList.postValue(animals)
            } else {
                // Handle empty list or null response
                _animalList.postValue(emptyList())  // Post an empty list if no animals are found
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
}

