package com.example.interview.models.responses.post.vacancy


import com.google.gson.annotations.SerializedName

data class VacancyRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("positionId")
    val positionId: Int,
    @SerializedName("structureId")
    val structureId: Int,
)