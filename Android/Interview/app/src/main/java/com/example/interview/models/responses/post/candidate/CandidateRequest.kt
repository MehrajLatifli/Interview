package com.example.interview.models.responses.post.candidate

import com.google.gson.annotations.SerializedName

data class CandidateRequest (
    @SerializedName("CandidateDocumentId")
    val candidateDocumentId: Int
)