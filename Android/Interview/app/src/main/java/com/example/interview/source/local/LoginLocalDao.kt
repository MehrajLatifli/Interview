package com.example.interview.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.interview.models.entities.LoginEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginLocalDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun add(entity: LoginEntity)

    @Query("SELECT * FROM logins")
    fun getWeatherEntities(): Flow<List<LoginEntity>>

    @Query("DELETE FROM logins WHERE Login_id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM logins WHERE username = :username")
    fun deleteByUsername(username: String)

    @Query("DELETE FROM logins")
    fun deleteAllUser()
}
