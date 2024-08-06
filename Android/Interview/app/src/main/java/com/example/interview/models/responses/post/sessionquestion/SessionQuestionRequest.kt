package com.example.interview.models.responses.get.sessionquestion


import com.google.gson.annotations.SerializedName

data class SessionQuestionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("Value")
    val value: Int?,
    @SerializedName("SessionId")
    val sessionId: Int?,
    @SerializedName("QuestionId")
    val questionId: Int?,

)