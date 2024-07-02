package com.itssvkv.todolist.ui.auth.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentLoginBinding
import com.itssvkv.todolist.utils.CommonFunctions
import com.itssvkv.todolist.utils.CommonFunctions.checkTheFormat
import com.itssvkv.todolist.utils.CommonFunctions.progressBarState
import com.itssvkv.todolist.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        init()
        progressBarState(state = false, binding.progressBar)
        return binding.root
    }

    private fun init() {
        onClickLoginButton()
        checkTheFormat(
            editText1 = binding.emailEt,
            editText2 = binding.passwordEt,
            button = binding.loginButton,
            context = requireContext()
        )
        onChangeInEditText()
        nextAction()
        doNotHaveAccount()
        initObservers()
    }

    private fun onChangeInEditText() {
        binding.emailEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.emailEt,
                editText2 = binding.passwordEt,
                button = binding.loginButton,
                context = requireContext()
            )
        }
        binding.passwordEt.doAfterTextChanged {
            checkTheFormat(
                editText1 = binding.emailEt,
                editText2 = binding.passwordEt,
                button = binding.loginButton,
                context = requireContext()
            )
        }
    }

    private fun onClickLoginButton() {
        binding.loginButton.setOnClickListener {
            CommonFunctions.buttonState(state = false, it, requireContext())
            login(
                email = binding.emailEt.text.toString().trim(),
                password = binding.passwordEt.text.toString().trim()
            )
        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.login(email = email, password = password)
    }

    private fun nextAction() {
        loginViewModel.loginSuccessful = {
            Log.d(Constants.TAG, "toasts: done")
            progressBarState(state = false, binding.progressBar)
            this@LoginFragment.findNavController()
                .navigate(R.id.action_loginFragment_to_homeFragment)
        }
        loginViewModel.loginFailed = {
            progressBarState(state = false, binding.progressBar)
            CommonFunctions.buttonState(state = true, binding.loginButton, requireContext())
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }

    }

    private fun doNotHaveAccount() {
        binding.createAccountTv.setOnClickListener {
            this@LoginFragment.findNavController().navigate(R.id.signUpFragment)
        }
    }

    private fun initObservers() {
        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                progressBarState(true, binding.progressBar)
                binding.loginButton.text = ""
            } else {
                progressBarState(false, binding.progressBar)
                binding.loginButton.text = resources.getString(R.string.login)
            }
        }
    }

}