package com.itssvkv.todolist.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.itssvkv.todolist.repository.datastore.DataStoreRepository
import com.itssvkv.todolist.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    var loginSuccessful: (() -> Unit)? = null
    var loginFailed: ((String?) -> Unit)? = null
    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.postValue(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _isLoading.postValue(false)
                loginSuccessful?.invoke()
                viewModelScope.launch {
                    dataStore.saveToDataStore(Constants.IS_LOGGED_IN, true)
                }
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
                loginFailed?.invoke(it.message)
            }
    }
}