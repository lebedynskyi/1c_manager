package com.simple.games.tradeassist.core.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.simple.games.tradeassist.data.AndroidNetworkStateDataSource
import com.simple.games.tradeassist.data.api.C1Api
import com.simple.games.tradeassist.data.db.DataBase
import com.simple.games.tradeassist.domain.NetworkStateDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun provideRoomDb(@ApplicationContext applicationContext: Context): DataBase {
        return Room.databaseBuilder(
            applicationContext,
            DataBase::class.java, "trade-assist.db"
        ).build()
    }

    @Provides
    fun provideRestRetrofit(
        httpClient: OkHttpClient,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults=true
            serializersModule = SerializersModule {
                contextual(String.serializer())
                contextual(Int.serializer())
                contextual(Double.serializer())
                contextual(Boolean.serializer())
                contextual(Float.serializer())
            }
        }

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://109.95.33.133:5252/krop/en_US/odata/standard.odata/")
            .addConverterFactory(json.asConverterFactory(contentType))

            .build()
    }

    @Provides
    fun provideHttpRestClient(
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.readTimeout(0, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(0, TimeUnit.SECONDS)
        val loggerInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        httpClientBuilder.addInterceptor(loggerInterceptor)
        return httpClientBuilder.build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit): C1Api {
        return retrofit.create(C1Api::class.java)
    }

    @Provides
    fun provideAndroidNetwork(
        @ApplicationContext context: Context,
        dispatcher: CoroutineScope
    ): NetworkStateDataSource {
        return AndroidNetworkStateDataSource(context, dispatcher)
    }

    @Provides
    fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(dispatcher)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}