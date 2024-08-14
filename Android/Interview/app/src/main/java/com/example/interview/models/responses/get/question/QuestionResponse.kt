package com.example.interview.models.responses.get.question


import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("levelId")
    val levelId: Int?,
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("structureId")
    val structureId: Int?
)