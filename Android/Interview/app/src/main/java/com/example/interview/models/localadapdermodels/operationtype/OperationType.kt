package com.example.interview.models.localadapdermodels.operationtype

import com.example.interview.models.localadapdermodels.operationcrud.Operation

data class OperationType (

    val image: Int,
    val text: String,
    val operations: List<Operation>

)