package com.venya.primeshow.domain.di

import android.content.Context
import androidx.room.Room
import com.venya.primeshow.BuildConfig
import com.venya.primeshow.data.local.MovieDatabase
import com.venya.primeshow.data.remote.ApiService
import com.venya.primeshow.utils.Constants
import com.venya.primeshow.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val contentType = "Content-Type"
    private const val accept = "accept"
    private const val contentTypeValue = "application/json"
    private const val authorization = "Authorization"
    private const val timeoutConnect = 30
    private const val timeoutRead = 30
    private const val bearerToken =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNzk0ZmE4YjRjNjkyNDAwYTE1N2IzODU4NDU0NDg4YyIsInN1YiI6IjY1ZGIxZjA0MDViNTQ5MDE2MjE2Y2JmOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.e1eZXEMOByj8BroU1BQVcA5NFkS5J9xP5srEd1byVgY"

    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            }
            return loggingInterceptor
        }


    private var headerInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        val request = originalRequest.newBuilder().header(contentType, contentTypeValue)
            .header(accept, contentTypeValue).addHeader(authorization, "Bearer $bearerToken")
            .method(originalRequest.method, originalRequest.body).build()
        chain.proceed(request)
    }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
        .readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            Constants.moviesDatabase // Use a constant or string literal for your database name
        ).fallbackToDestructiveMigration() // Handle migrations
            .build()
    }

}