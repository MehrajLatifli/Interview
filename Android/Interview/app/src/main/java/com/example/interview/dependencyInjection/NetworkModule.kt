package com.example.interview.dependencyInjection


import com.example.interview.source.api.ApiKeyInterceptor
import com.example.interview.source.api.IApiManager
import com.example.interview.utilities.Constants.Base_URL
import com.squareup.picasso.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideApiInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }


    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor
    {
        val httpLoggingInterceptor = HttpLoggingInterceptor ()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideHttpClint(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(68, TimeUnit.SECONDS)
            .addInterceptor (httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient:OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Base_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideIApiManager(retrofit: Retrofit): IApiManager {
        return  retrofit.create(IApiManager::class.java)
    }


}