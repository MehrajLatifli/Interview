package com.example.interview.models.responses.post.vacancy


import com.google.gson.annotations.SerializedName

data class Vacancy(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("StartDate")
    val startDate: String,
    @SerializedName("EndDate")
    val endDate: String,
    @SerializedName("PositionId")
    val positionId: Int,
    @SerializedName("StructureId")
    val structureId: Int,
)