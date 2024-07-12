package com.example.interview.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.interview.models.entities.LoginEntity

@Database(entities = [LoginEntity::class], version = 2)
abstract class LoginDatabase : RoomDatabase(){
    abstract fun createDAO(): LoginLocalDao
}