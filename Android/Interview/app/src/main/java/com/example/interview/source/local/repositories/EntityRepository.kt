package com.example.interview.source.local.repositories

import com.example.interview.models.entities.LoginEntity
import com.example.interview.source.local.LoginLocalDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EntityRepository  @Inject constructor(
    val loginLocalDao: LoginLocalDao
){
    fun addWeatherEntity(weatherEntity: LoginEntity) = loginLocalDao.add(weatherEntity)


    fun getWeatherEntities(): Flow<List<LoginEntity>> {
        return loginLocalDao.getWeatherEntities().flowOn(Dispatchers.IO)
    }

    fun deleteByUsername(username:String) {
        return loginLocalDao.deleteByUsername(username)
    }

    fun deleteAllUser() {
        return loginLocalDao.deleteAllUser()
    }
}