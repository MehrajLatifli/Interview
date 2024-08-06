package com.example.interview.models.responses.post.question


import com.google.gson.annotations.SerializedName

data class QuestionRequest(
    @SerializedName("text")
    val text: String?,
    @SerializedName("levelId")
    val levelId: Int?,
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("structureId")
    val structureId: Int?
)