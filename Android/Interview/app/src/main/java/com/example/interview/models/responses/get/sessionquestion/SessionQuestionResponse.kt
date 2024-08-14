package com.example.interview.models.responses.get.sessionquestion


import com.google.gson.annotations.SerializedName

data class SessionQuestionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("value")
    val value: Int?,
    @SerializedName("sessionId")
    val sessionId: Int?,
    @SerializedName("questionId")
    val questionId: Int?,

)