package com.example.interview.models.localadapdermodels.operationtype

import com.example.interview.models.localadapdermodels.operationcrud.OperationCRUD

data class OperationType (

    val image: Int,
    val text: String,
    val operationCRUDs: List<OperationCRUD>

)