package com.example.nanomedic.guides

data class WoundGuide(
    val id: String, // Unique identifier for the wound
    val type: String, // e.g., "Cut", "Burn", "Abrasion"
    val treatmentSteps: List<String>, // List of steps for treatment
    val description: String // More detailed description
)