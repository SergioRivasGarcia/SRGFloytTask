package com.example.app.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.example.data.network.RetrofitBase
import com.example.data.products.database.ProductDatabase
import com.example.domain.products.repository.IProductDBRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val READ_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 45L

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun retrofit(retrofitBase: RetrofitBase): Retrofit = retrofitBase.retrofit

    @Provides
    @Singleton
    fun resources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()


    @Provides
    @Singleton
    fun okHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return builder
            .addInterceptor(interceptor)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun productDatabase(application: Application): ProductDatabase {
        return Room
            .databaseBuilder(application, ProductDatabase::class.java, "product.db")
            .build()
    }

    @Provides
    fun iProductDBRepository(database: ProductDatabase): IProductDBRepository =
        database.iProductDBRepository()
}