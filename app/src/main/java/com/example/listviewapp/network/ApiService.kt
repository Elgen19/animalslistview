package com.example.listviewapp.network

import android.util.Log
import com.example.listviewapp.model.Animal
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

object ApiService {

    private const val API_URL = "https://cab46cf5c84168fdfe1f.free.beeceptor.com/api/animal/"

    // Function to fetch animals from the API using HttpURLConnection
    fun getAnimals(onResult: (List<Animal>?) -> Unit) {
        Thread {
            try {
                // Create a URI object from the URL string
                val uri = URI.create(API_URL)

                // Create a URL object
                val url = URL(uri.toString())

                // Open a connection to the API
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                // Read the response
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                // Parse the response into a list of Animal objects
                val animalList = parseAnimalResponse(response.toString())

                // Return the result using the callback
                onResult(animalList)

                // Close the connection
                connection.disconnect()

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching animals: ${e.message}")
                onResult(null) // In case of error, return null
            }
        }.start()
    }

    // Function to delete an animal based on its ID
    fun deleteAnimal(animalId: Int, onResult: (Boolean) -> Unit) {
        Thread {
            try {
                // Construct the URL for the delete request (append the animal ID)
                val url = URL("$API_URL$animalId")

                // Open the connection
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Send the request and get the response code
                val responseCode = connection.responseCode
                val inputStream = if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }
                val responseMessage = inputStream.bufferedReader().use { it.readText() }
                Log.d("ApiService", "Response: $responseMessage")

                // Check if deletion was successful
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
                    onResult(true)
                } else {
                    onResult(false)
                }

                // Close the connection
                connection.disconnect()

            } catch (e: Exception) {
                Log.e("ApiService", "Error deleting animal: ${e.message}")
                onResult(false) // In case of error, return false
            }
        }.start()
    }

    // Helper function to parse the JSON response into a list of Animal objects
    private fun parseAnimalResponse(response: String): List<Animal> {
        val animalList = mutableListOf<Animal>()
        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val animalJson = jsonArray.getJSONObject(i)
                val id = animalJson.getInt("id")
                val name = animalJson.getString("name")
                animalList.add(Animal(id, name))
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Error parsing JSON: ${e.message}")
        }
        return animalList
    }
}
