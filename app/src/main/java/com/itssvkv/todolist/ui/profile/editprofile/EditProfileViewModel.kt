package com.itssvkv.todolist.ui.profile.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.repository.firebase.FirebaseRepository
import com.itssvkv.todolist.utils.Constants
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val storageReference: StorageReference
) : ViewModel() {

    private var _currentUserInfoLiveData = MutableLiveData<UserDetails>()
    val currentUserInfoLiveData: LiveData<UserDetails> = _currentUserInfoLiveData

    //    private var _updateCurrentUserInfo = MutableLiveData<UserDetails>()
//    val updateCurrentUserInfo : LiveData<UserDetails> = _updateCurrentUserInfo
    private var _currentUserInfo: UserDetails? = null
    private var updatedUserInfo: UserDetails? = null
    private val _imageUriLiveData = MutableLiveData<Uri>()

    var updatedSuccessful: (() -> Unit)? = null
    var updatedFailed: ((String?) -> Unit)? = null
    private var _isUpdated = MutableLiveData<Boolean>(false)
    val isUpdated: LiveData<Boolean> = _isUpdated


    init {
        getUserInfo()
    }


    private fun getUserInfo() {
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().get().addOnSuccessListener { userInfo ->
                _currentUserInfoLiveData.postValue(userInfo.toObject(UserDetails::class.java))
                _currentUserInfo = (userInfo.toObject(UserDetails::class.java))
            }.addOnFailureListener {
                Log.d(Constants.TAG, "getUserInfo: ${it.message}")
            }
        }
    }

    fun uploadImageToFirebase(image: Uri) {
        val imageRef = storageReference.child(UUID.randomUUID().toString())
        imageRef.putFile(image).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                _imageUriLiveData.postValue(it)

            }
        }
    }

    fun updateCurrentUserInfo(
        name: String,
        userName: String,
        aboutYou: String,
        address: String

    ) {
        _isUpdated.postValue(true)
        updatedUserInfo = if (_imageUriLiveData.value == null) {
            Log.d(TAG, "updateCurrentUserInfo: image null")
            _currentUserInfo?.copy(
                name = name,
                aboutYou = aboutYou,
                username = userName,
                address = address
            )

        } else {
            Log.d(TAG, "updateCurrentUserInfo: image not null")
            _currentUserInfo?.copy(
                name = name,
                aboutYou = aboutYou,
                photo = _imageUriLiveData.value.toString(),
                username = userName,
                address = address
            )
        }
        viewModelScope.launch {
            firebaseRepository.currentUserDetails().set(updatedUserInfo!!).addOnSuccessListener {
                _isUpdated.postValue(false)
                updatedSuccessful?.invoke()

            }.addOnFailureListener {
                _isUpdated.postValue(false)
                updatedFailed?.invoke(it.toString())

            }
        }
    }


}
