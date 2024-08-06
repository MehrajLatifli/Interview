package com.example.interview.models.responses.post.sessionquestion


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