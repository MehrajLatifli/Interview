package com.example.interview.models.responses.post.sessionquestion

import com.google.gson.annotations.SerializedName

data class RandomQuestionRequestModel2(
    @SerializedName("questionCount") val questionCount: Int,
    @SerializedName("vacantionId") val vacantionId: Int,
    @SerializedName("sessionId") val sessionId: Int
)