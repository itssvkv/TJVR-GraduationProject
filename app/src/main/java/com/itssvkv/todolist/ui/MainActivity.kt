package com.itssvkv.todolist.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.ActivityMainBinding
import com.itssvkv.todolist.utils.Constants.TAG
import com.itssvkv.todolist.utils.Constants.isFirstTime
import com.itssvkv.todolist.utils.Constants.isLoggedIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val isKeepSplashScreen = MutableLiveData(true)
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        delaySplashScreen()
        navigation()
        installSplashScreen().setKeepOnScreenCondition {
            isKeepSplashScreen.value ?: true
        }
        Log.d(TAG, "onCreate: ggggggggggggg")
        setContentView(binding.root)
    }

    private fun delaySplashScreen() {
        lifecycleScope.launch {
            delay(2000)
            isKeepSplashScreen.value = false
        }
    }

    private fun navigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                    as NavHostFragment
        navController = navHostFragment.navController

        if (!isFirstTime) {
            if (isLoggedIn) {
                navController
                    .navigate(R.id.action_onBoardingFragment_to_homeFragment)
            } else {
                navController
                    .navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
