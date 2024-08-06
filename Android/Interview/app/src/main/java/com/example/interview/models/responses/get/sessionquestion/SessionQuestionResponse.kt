package com.example.interview.models.responses.get.sessionquestion


import com.google.gson.annotations.SerializedName

data class SessionQuestionResponse(
    @SerializedName("QuestionId")
    val questionId: Int? = null,
    @SerializedName("SessionId")
    val sessionId: Int? = null,
    @SerializedName("Value")
    val value: Int? = null
)