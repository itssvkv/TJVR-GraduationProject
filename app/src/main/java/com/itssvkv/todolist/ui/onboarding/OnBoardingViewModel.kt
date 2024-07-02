package com.itssvkv.todolist.ui.onboarding

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.itssvkv.todolist.repository.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    suspend fun <T> saveToDataStore(key: Preferences.Key<T>, value: T) {
        dataStoreRepository.saveToDataStore(key = key, value = value)
    }
}
