package com.example.interview.models.responses.get.vacancy


import com.google.gson.annotations.SerializedName

data class VacancyResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("positionId")
    val positionId: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("structureId")
    val structureId: Int,
    @SerializedName("title")
    val title: String
)