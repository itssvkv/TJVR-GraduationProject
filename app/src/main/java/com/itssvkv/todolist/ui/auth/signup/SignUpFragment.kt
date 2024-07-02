package com.itssvkv.todolist.ui.auth.signup

import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentSignUpBinding
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import com.itssvkv.todolist.utils.CommonFunctions.buttonState
import com.itssvkv.todolist.utils.CommonFunctions.checkTheFormat
import com.itssvkv.todolist.utils.CommonFunctions.progressBarState

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val signUpViewModel by viewModels<SignUpViewModel>()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var userDetails: UserDetails? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        init()
        progressBarState(state = false, binding.progressBar)
        return binding.root
    }

    private fun init() {
        onClickSignUpButton()
        nextAction()
        checkTheFormat(
            editText1 = binding.nameEt,
            editText2 = binding.emailEt,
            editText3 = binding.passwordEt,
            editText4 = binding.confirmPasswordEt,
            button = binding.singUpButton,
            context = requireContext()
        )
        onChangeInEditText()
        confirmPassword()
        alreadyHaveAccount()
        initObservers()
    }

    private fun onClickSignUpButton() {
        binding.singUpButton.setOnClickListener {
            buttonState(state = false, it, requireContext())
            userDetails = UserDetails(
                name = binding.nameEt.text.toString().trim(),
                email = binding.emailEt.text.toString().trim(),
                password = binding.passwordEt.text.toString().trim()
            )
            signUp(userDetails)
        }
    }

    private fun onChangeInEditText() {
        binding.emailEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.nameEt,
                editText2 = binding.emailEt,
                editText3 = binding.passwordEt,
                editText4 = binding.confirmPasswordEt,
                button = binding.singUpButton,
                context = requireContext()
            )
        }
        binding.passwordEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.nameEt,
                editText2 = binding.emailEt,
                editText3 = binding.passwordEt,
                editText4 = binding.confirmPasswordEt,
                button = binding.singUpButton,
                context = requireContext()
            )
        }
        binding.confirmPasswordEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.nameEt,
                editText2 = binding.emailEt,
                editText3 = binding.passwordEt,
                editText4 = binding.confirmPasswordEt,
                button = binding.singUpButton,
                context = requireContext()
            )
        }
        binding.nameEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.nameEt,
                editText2 = binding.emailEt,
                editText3 = binding.passwordEt,
                editText4 = binding.confirmPasswordEt,
                button = binding.singUpButton,
                context = requireContext()
            )
        }
    }

    private fun confirmPassword() {
        binding.confirmPasswordEt.doAfterTextChanged {
            if (binding.passwordEt.text.toString().trim() != it.toString().trim()) {
                binding.confirmPasswordEt.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.edit_text_error,
                    resources.newTheme()
                )
                buttonState(state = false, binding.singUpButton, requireContext())
            } else {
                binding.confirmPasswordEt.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.primary_edit_text,
                    resources.newTheme()
                )
                buttonState(state = true, binding.singUpButton, requireContext())
            }
        }
    }


    private fun signUp(
        userDetails: UserDetails?
    ) {
        signUpViewModel.signUp(userDetails)
    }

    private fun nextAction() {
        signUpViewModel.signUpSuccessful = {
            Log.d(TAG, "toasts: done")
            progressBarState(state = false, binding.progressBar)
            this@SignUpFragment.findNavController()
                .navigate(R.id.action_signUpFragment_to_homeFragment)
        }
        signUpViewModel.signUpFailed = {
            progressBarState(state = false, binding.progressBar)
            buttonState(state = true, binding.singUpButton, requireContext())
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alreadyHaveAccount() {
        binding.haveAccountTv.setOnClickListener {
            this@SignUpFragment.findNavController()
                .navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun initObservers() {
        signUpViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                progressBarState(true, binding.progressBar)
                binding.singUpButton.text = ""
            } else {
                progressBarState(false, binding.progressBar)
                binding.singUpButton.text = resources.getString(R.string.sign_up)
            }
        }
    }

}