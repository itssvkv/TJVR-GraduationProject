package com.itssvkv.todolist.repository.datastore

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun <T> saveToDataStore(key: Preferences.Key<T>, value: T)
    suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T?
}
