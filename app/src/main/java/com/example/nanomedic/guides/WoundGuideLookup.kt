package com.example.nanomedic.guides

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class WoundGuideLookup(private val context: Context) {

    // Lazy initialization of the list of wound guides with a renamed private property
    private val _allWoundGuides: List<WoundGuideJsonEntry> by lazy {
        loadWoundGuidesFromFile() ?: emptyList()
    }

    // Private function to load and parse the JSON data from assets
    private fun loadWoundGuidesFromFile(): List<WoundGuideJsonEntry>? {
        return try {
            // Open the JSON file from assets
            val inputStream = context.assets.open("wound-guides.json")
            // Read the content into a string
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)

            // Use Gson to parse the JSON string into a List of WoundGuideJsonEntry
            val gson = Gson()
            val listType = object : TypeToken<List<WoundGuideJsonEntry>>() {}.type
            gson.fromJson<List<WoundGuideJsonEntry>>(jsonString, listType)

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle error appropriately here
            null
        }
    }

    // Public getter to retrieve all wound guides
    fun getAllWoundGuides(): List<WoundGuideJsonEntry> {
        return _allWoundGuides
    }

    // Public getter to retrieve a specific wound guide by its woundType
    fun getWoundGuideByType(woundType: String): WoundGuideJsonEntry? {
        return _allWoundGuides.find { it.woundType.equals(woundType, ignoreCase = true) }
    }
}
