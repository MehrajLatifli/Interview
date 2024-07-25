package com.example.interview.models.responses.get.candidate


import com.google.gson.annotations.SerializedName

data class CandidateResponse(
    @SerializedName("CandidateDocumentId")
    val candidateDocumentId: Int
)