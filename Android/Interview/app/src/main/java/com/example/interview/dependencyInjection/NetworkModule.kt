package com.example.interview.dependencyInjection

import android.content.Context
import com.example.interview.source.api.interceptors.ApiKeyInterceptor
import com.example.interview.source.api.ApiKeyProvider
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.RefreshTokenDetector
import com.example.interview.utilities.Constants.Base_URL
import com.example.interview.utilities.createUnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Dns
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
    fun provideApiKeyProvider(@ApplicationContext context: Context): ApiKeyProvider {
        return ApiKeyProvider(context)
    }

    @Singleton
    @Provides
    fun provideApiInterceptor(apiKeyProvider: ApiKeyProvider): ApiKeyInterceptor {
        return ApiKeyInterceptor(apiKeyProvider)
    }

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient {
        return createUnsafeOkHttpClient().newBuilder()
            .dns(Dns.SYSTEM)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Base_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideIApiManager(retrofit: Retrofit): IApiManager {
        return retrofit.create(IApiManager::class.java)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenDetector(apiManager: IApiManager, @ApplicationContext  context: Context): RefreshTokenDetector {
        return RefreshTokenDetector(apiManager, context)
    }
}
