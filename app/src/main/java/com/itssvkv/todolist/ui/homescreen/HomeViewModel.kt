package com.itssvkv.todolist.ui.homescreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val storageReference: StorageReference
) : ViewModel() {
    private var _currentUserInfo = MutableLiveData<UserDetails>()
    val currentUserInfo: LiveData<UserDetails> = _currentUserInfo

    var checkResultSuccessful: (() -> Unit)? = null
    var checkResultFailed: ((String?) -> Unit)? = null
    private var _isCheckResult = MutableLiveData(false)
    val isCheckResult: LiveData<Boolean> = _isCheckResult
    private val _imageUriLiveData = MutableLiveData<Uri>()
    val imageUriLiveData : LiveData<Uri> = _imageUriLiveData

    var imageUploadSuccessful : (() -> Unit)? = null
    var imageUploadFailed : ((String) -> Unit)? = null

    var loginSuccessful: (() -> Unit)? = null
    var loginFailed: ((String?) -> Unit)? = null
    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        getUserInfo()
    }


    private fun getUserInfo() {
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().get().addOnSuccessListener { userInfo ->
                _currentUserInfo.postValue(userInfo.toObject(UserDetails::class.java))
            }.addOnFailureListener {
                Log.d(TAG, "getUserInfo: ${it.message}")
            }
        }
    }

    fun checkResult(){
        _isCheckResult.postValue(true)
        viewModelScope.launch {
            delay(1500)
            _isCheckResult.postValue(false)
            checkResultSuccessful?.invoke()
        }

    }

    fun uploadImageToFirebase(image: Uri) {
        _isLoading.postValue(true)
        val imageRef = storageReference.child(UUID.randomUUID().toString())
        imageRef.putFile(image).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                Log.d("itssvkv", "uploadImageToFirebase: $it")
                _isLoading.postValue(false)
                _imageUriLiveData.postValue(it)
                imageUploadSuccessful?.invoke()
//                loginSuccessful?.invoke()
            }.addOnFailureListener {
                _isLoading.postValue(false)
                imageUploadFailed?.invoke(it.toString())
//                loginFailed?.invoke(it.toString())
            }
        }
    }

}