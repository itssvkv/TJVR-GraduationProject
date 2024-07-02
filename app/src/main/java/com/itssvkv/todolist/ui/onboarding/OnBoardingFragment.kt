package com.itssvkv.todolist.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentOnBoardingBinding
import com.itssvkv.todolist.ui.onboarding.pager.PagerAdapter
import com.itssvkv.todolist.utils.Constants.IS_FIRST_TIME
import com.itssvkv.todolist.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private lateinit var pagerAdapter: PagerAdapter
    private val binding get() = _binding!!
    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun changeIvColor(color: Int) {
        binding.iv.setImageResource(color)
    }

    private fun init() {
        initAdapter()
        setNextBtnVisibility()
        initNextBtn()
        initSkipTV()
        lifecycleScope.launch {
            onBoardingViewModel.saveToDataStore(IS_FIRST_TIME, false)
        }
    }

    private fun initAdapter() {
        pagerAdapter = PagerAdapter(this)
        binding.onBoardingPager.adapter = pagerAdapter
    }

    private fun setNextBtnVisibility() {
        binding.onBoardingPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (position) {
                    0 -> {
                        binding.onBoardingNextTv.visibility = View.VISIBLE
                        binding.onBoardingSkipTv.visibility = View.VISIBLE
                        binding.onBoardingContinueTv.visibility = View.INVISIBLE
                        changeIvColor(R.drawable.on_boarding_color)
                    }

                    1 -> {
                        binding.onBoardingNextTv.visibility = View.VISIBLE
                        binding.onBoardingSkipTv.visibility = View.VISIBLE
                        binding.onBoardingContinueTv.visibility = View.INVISIBLE
                        changeIvColor(R.drawable.on_boarding_sec_color)
                    }

                    else -> {
                        Log.d(TAG, "onPageScrolled: $position")
                        binding.onBoardingNextTv.visibility = View.INVISIBLE
                        binding.onBoardingSkipTv.visibility = View.INVISIBLE
                        binding.onBoardingContinueTv.visibility = View.VISIBLE
                        changeIvColor(R.drawable.on_boarding_third_color)
                    }
                }
            }
        })
    }

    private fun initNextBtn() {
        binding.onBoardingNextTv.setOnClickListener {
            when (binding.onBoardingPager.currentItem) {
                0 -> {
                    binding.onBoardingPager.currentItem = 1
                }

                1 -> {
                    binding.onBoardingPager.currentItem = 2
                }
            }
        }
    }

    private fun initSkipTV() {
        binding.onBoardingSkipTv.setOnClickListener {
            this@OnBoardingFragment.findNavController()
                .navigate(R.id.action_onBoardingFragment_to_loginFragment)
            lifecycleScope.launch {
                onBoardingViewModel.saveToDataStore(IS_FIRST_TIME, false)
            }
        }
        binding.onBoardingContinueTv.setOnClickListener {
            this@OnBoardingFragment.findNavController()
                .navigate(R.id.action_onBoardingFragment_to_loginFragment)
            lifecycleScope.launch {
                onBoardingViewModel.saveToDataStore(IS_FIRST_TIME, false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
