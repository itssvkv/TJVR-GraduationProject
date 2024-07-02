package com.itssvkv.todolist

import android.app.Application
import com.itssvkv.todolist.repository.datastore.DataStoreRepository
import com.itssvkv.todolist.utils.Constants
import com.itssvkv.todolist.utils.Constants.IS_FIRST_TIME
import com.itssvkv.todolist.utils.Constants.IS_LOGGED_IN
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ToDoListApplication : Application() {
    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        job = CoroutineScope(Dispatchers.IO).launch {
            val isLoggedIn = dataStoreRepository.getFromDataStore(key = IS_LOGGED_IN) ?: false
            val isFirstTime = dataStoreRepository.getFromDataStore(key = IS_FIRST_TIME) ?: true

            Constants.isLoggedIn = isLoggedIn
            Constants.isFirstTime = isFirstTime
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        job?.cancel()
    }

    override fun onTerminate() {
        super.onTerminate()
        job?.cancel()
    }
}
