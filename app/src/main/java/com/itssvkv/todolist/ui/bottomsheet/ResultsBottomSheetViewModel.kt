package com.itssvkv.todolist.ui.bottomsheet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsBottomSheetViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {


    private var _currentUserInfo = MutableLiveData<UserDetails>()
    val currentUserInfo: LiveData<UserDetails> = _currentUserInfo


    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var isUploadedSuccessfully: (() -> Unit)? = null
    var isUploadedFailure: ((String) -> Unit)? = null

    init {
        getCurrentUserInfo()
    }


    private fun getCurrentUserInfo() {
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().get().addOnSuccessListener {
                _currentUserInfo.postValue(it.toObject(UserDetails::class.java))
            }.addOnFailureListener {
                Log.d(TAG, "getCurrentUserInfo: ${it.message}")
            }
        }
    }


    fun updateCurrentUserInfo(diagnosis: String) {
        _isLoading.postValue(true)
        _currentUserInfo.value?.let { userDetails ->
            val updatedUserInfo = userDetails.copy(
                name = userDetails.name,
                email = userDetails.email,
                username = userDetails.username,
                photo = userDetails.photo,
                password = userDetails.password,
                aboutYou = userDetails.aboutYou,
                address = userDetails.address,
                diagnosis = diagnosis
            )
            viewModelScope.launch {
                delay(1500)
                firebaseRepository.currentUserDetails().set(updatedUserInfo).addOnSuccessListener {
                    _isLoading.postValue(false)
                    isUploadedSuccessfully?.invoke()
                }.addOnFailureListener {
                    _isLoading.postValue(false)
                    isUploadedFailure?.invoke(it.message!!)
                }
            }
        }

    }
}