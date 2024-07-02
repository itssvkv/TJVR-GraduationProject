package com.itssvkv.todolist.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentProfileBinding
import com.itssvkv.todolist.model.UserDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() =  _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var currentUserInfo: UserDetails? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        initClicks()
        getUserInfo()
    }

    private fun getUserInfo(){
        profileViewModel.currentUserInfo.observe(viewLifecycleOwner){
            currentUserInfo = it
            setupUserInfo()
        }
    }

    private fun initClicks() {
        binding.editBackgroundIv.setOnClickListener {
            this@ProfileFragment.findNavController()
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.backIv.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun setupUserInfo() {
        Glide.with(binding.profileIv.context).load(currentUserInfo?.photo)
            .apply(RequestOptions().dontTransform())
            .into(binding.profileIv)
        binding.usernameTv.text = currentUserInfo?.name
        binding.theUserUsernameTv.text = currentUserInfo?.username
        binding.userAddressTv.text = currentUserInfo?.address
        binding.theAboutYouTvTv.text = currentUserInfo?.aboutYou
        binding.userEmailTv.text = currentUserInfo?.email
        binding.theDiagnosisTv.text = currentUserInfo?.diagnosis
    }
}