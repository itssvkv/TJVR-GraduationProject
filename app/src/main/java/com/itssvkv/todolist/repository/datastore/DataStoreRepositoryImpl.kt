package com.itssvkv.todolist.repository.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.itssvkv.todolist.utils.Constants.TAG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) :
    DataStoreRepository {
    override suspend fun <T> saveToDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    override suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T? {
        return dataStore.data
            .catch { throwable ->
                Log.d(TAG, "getFromDataStore: $throwable")
            }.map { it[key] }
            .firstOrNull()
    }

}
