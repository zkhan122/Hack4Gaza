package com.example.nanomedic.guides

import com.google.gson.annotations.SerializedName

// This data class directly maps to one of the JSON objects in your array
data class WoundGuideJsonEntry(
    val id: Int,
    @SerializedName("wound-type") val woundType: String,
    // Using Map<String, String> to flexibly handle "Step-1", "Step-2", etc.
    @SerializedName("treatment-eng") val treatmentEng: List<Map<String, String>>,
    @SerializedName("treatment-arab") val treatmentArab: List<Map<String, String>>,
    @SerializedName("identification-eng") val identificationEng: String,
    @SerializedName("identification-arab") val identificationArab: String
)