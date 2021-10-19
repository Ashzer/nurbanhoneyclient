package org.devjj.platform.nurbanhoney.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.devjj.platform.nurbanhoney.AndroidApplication
import org.devjj.platform.nurbanhoney.BuildConfig
import org.devjj.platform.nurbanhoney.features.ui.login.LoginManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://3.37.155.214:8080/")
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideAppUpdateManager(application: Application) : AppUpdateManager {
        return AppUpdateManagerFactory.create(application)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) : SharedPreferences{
        return context.getSharedPreferences("prefs",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLoginManager(dataSource : LoginManager.Network) : LoginManager = dataSource

}