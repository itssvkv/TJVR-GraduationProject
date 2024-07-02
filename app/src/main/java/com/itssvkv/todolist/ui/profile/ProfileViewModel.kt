package com.itssvkv.todolist.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val storageReference: StorageReference

) : ViewModel() {

    private var _currentUserInfo = MutableLiveData<UserDetails>()
    val currentUserInfo: LiveData<UserDetails> = _currentUserInfo

    private var _updateCurrentUserInfo = MutableLiveData<UserDetails>()
    val updateCurrentUserInfo : LiveData<UserDetails> = _updateCurrentUserInfo

    init {
        getUserInfo()
    }


    private fun getUserInfo() {
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().get().addOnSuccessListener { userInfo ->
                _currentUserInfo.postValue(userInfo.toObject(UserDetails::class.java))
            }.addOnFailureListener {
                Log.d(Constants.TAG, "getUserInfo: ${it.message}")
            }
        }
    }

}