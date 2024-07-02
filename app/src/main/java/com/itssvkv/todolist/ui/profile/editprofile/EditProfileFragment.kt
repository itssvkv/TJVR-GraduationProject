package com.itssvkv.todolist.ui.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentEditProfileBinding
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.utils.CommonFunctions
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var imgResult: Uri? = null
    private val imgIntent by lazy { Intent() }
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private var currentUserInfo: UserDetails? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        getUserInfo()
        initClicks()
        selectImageFormGallery()
        initIsUpdatedObserver()
    }

    private fun getUserInfo() {
        editProfileViewModel.currentUserInfoLiveData.observe(viewLifecycleOwner) {
            currentUserInfo = it
            setupUserInfo()
            Log.d(TAG, "getUserInfo: $currentUserInfo")
        }
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.profileIv.setOnClickListener {
            resultLauncher?.launch(imgIntent)
        }
        binding.editProfileButton.setOnClickListener {
            editProfileViewModel.updateCurrentUserInfo(
                name = binding.updateNameEt.text.toString().trim(),
                userName = binding.updateUsernameEt.text.toString().trim(),
                aboutYou = binding.updateStatusEt.text.toString().trim(),
                address = binding.updateAddressEt.text.toString().trim()
            )
        }
    }

    private fun selectImageFormGallery() {
        imgIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    imgResult = it.data?.data
                    binding.profileIv.apply {
                        Log.d(TAG, "selectImageFormGallery: $imgResult")
                        setImageURI(imgResult)
                        editProfileViewModel.uploadImageToFirebase(imgResult!!)
                    }
                }
            }
    }

    private fun setupUserInfo() {
        Glide.with(binding.profileIv.context).load(currentUserInfo?.photo)
            .apply(RequestOptions().dontTransform())
            .into(binding.profileIv)
        binding.updateNameEt.setText(currentUserInfo?.name)
        binding.updateUsernameEt.setText(currentUserInfo?.username)
        binding.updateStatusEt.setText(currentUserInfo?.aboutYou)
        binding.updateAddressEt.setText(currentUserInfo?.address)
    }

    private fun initIsUpdatedObserver() {
        editProfileViewModel.isUpdated.observe(viewLifecycleOwner) {
            if (it) {
                CommonFunctions.buttonState(
                    state = !it,
                    binding.editProfileButton,
                    requireContext()
                )
                CommonFunctions.progressBarState(state = it, binding.progressBar)
                binding.editProfileButton.text = ""
            } else {
                binding.editProfileButton.text = resources.getString(R.string.confirm)
            }
        }
        editProfileViewModel.updatedSuccessful = {
            Toast.makeText(requireContext(), "update successful", Toast.LENGTH_SHORT).show()
            this@EditProfileFragment.findNavController().popBackStack()
        }
        editProfileViewModel.updatedFailed = {
            CommonFunctions.buttonState(
                state = true,
                binding.editProfileButton,
                requireContext()
            )
            CommonFunctions.progressBarState(state = false, binding.progressBar)
            binding.editProfileButton.text = resources.getString(R.string.confirm)
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}