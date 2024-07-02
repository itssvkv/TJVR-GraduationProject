package com.itssvkv.todolist.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itssvkv.todolist.data.local.room.TasksDao
import com.itssvkv.todolist.data.local.room.TasksDatabase
import com.itssvkv.todolist.repository.datastore.DataStoreRepository
import com.itssvkv.todolist.repository.datastore.DataStoreRepositoryImpl
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.repository.firebase.FirebaseRepositoryImpl
import com.itssvkv.todolist.repository.room.RoomRepository
import com.itssvkv.todolist.repository.room.RoomRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)

    }

    @Provides
    @Singleton
    fun providesRoomRepository(tasksDao: TasksDao, db: TasksDatabase): RoomRepository {
        return RoomRepositoryImpl(tasksDao, db)
    }

    @Provides
    @Singleton
    fun providesFirebaseRepository(auth: FirebaseAuth, db:FirebaseFirestore): FirebaseRepository {
        return FirebaseRepositoryImpl(auth, db)
    }
}
