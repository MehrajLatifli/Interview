package com.example.interview.models.responses.post.sessionquestion


import com.google.gson.annotations.SerializedName

data class SessionQuestionRequest(
    @SerializedName("value")
    val value: Int?,
    @SerializedName("sessionId")
    val sessionId: Int?,
    @SerializedName("questionId")
    val questionId: Int?,

)