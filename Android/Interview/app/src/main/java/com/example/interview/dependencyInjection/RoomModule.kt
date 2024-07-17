package com.example.interview.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.example.interview.source.local.LoginDatabase
import com.example.interview.source.local.LoginLocalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideLocalDatabase(application: Application): LoginDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            LoginDatabase::class.java,
            "LogIn_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideLocalDao(localDatabase: LoginDatabase): LoginLocalDao {
        return localDatabase.createDAO()
    }
}
