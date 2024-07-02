package com.itssvkv.todolist.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.repository.datastore.DataStoreRepository
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.utils.Constants.IS_LOGGED_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStoreRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    var signUpSuccessful: (() -> Unit)? = null
    var signUpFailed: ((String?) -> Unit)? = null
    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun signUp(userDetails: UserDetails?) {
        _isLoading.postValue(true)
        auth.createUserWithEmailAndPassword(userDetails?.email!!, userDetails.password)
            .addOnSuccessListener {
                _isLoading.postValue(false)
                viewModelScope.launch {
                    dataStore.saveToDataStore(IS_LOGGED_IN, true)
                }
                setUserDataToFirebase(userDetails = userDetails)
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
                signUpFailed?.invoke(it.message)
            }
    }

    private fun setUserDataToFirebase(userDetails: UserDetails?) {
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().set(userDetails!!).addOnSuccessListener {
                signUpSuccessful?.invoke()
            }
                .addOnFailureListener {
                    signUpFailed?.invoke(it.message)
                }
        }
    }
}