package com.example.interview.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logins")
data class LoginEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Login_id")
    val id: Int = 0,

    val username: String,
    val token: String,
)
