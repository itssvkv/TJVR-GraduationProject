package com.itssvkv.todolist.di

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.itssvkv.todolist.data.local.room.TasksDatabase
import com.itssvkv.todolist.data.network.ApiCalls
import com.itssvkv.todolist.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val Context.appDataStore by preferencesDataStore(name = Constants.APP_NAME)

    @Provides
    @Singleton
    fun providesDataStorePreferences(@ApplicationContext appContext: Context):
            DataStore<Preferences> {
        return appContext.appDataStore
    }

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = TasksDatabase::class.java,
            name = "TasksDatabase"
        )
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesTasksDao(db: TasksDatabase) = db.getTaskDao()

    @Provides
    @Singleton
    fun providesFireAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseStorage() : StorageReference {
        return FirebaseStorage.getInstance().reference
    }


    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val httpClientLoggingInterceptor = HttpLoggingInterceptor { msg ->
            Log.i("logInterceptor", "Interceptor : $msg")
        }
        httpClientLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder().apply {
            addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Accept", "application/json")
                chain.proceed(newRequest.build())
            }
            addInterceptor(httpClientLoggingInterceptor)
        }.build()
    }


    @Provides
    @Singleton
    fun providesRetrofitServers(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(Constants.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    fun providesApiCalls(retrofit: Retrofit): ApiCalls {
        return retrofit.create(ApiCalls::class.java)
    }

    @Provides
    @Singleton
    fun providesBundle() : Bundle{
        return  Bundle()
    }

}
