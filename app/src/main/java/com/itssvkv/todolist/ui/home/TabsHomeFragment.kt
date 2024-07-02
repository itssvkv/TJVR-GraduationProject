package com.itssvkv.todolist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentHomeBinding
import com.itssvkv.todolist.databinding.FragmentTabsHomeBinding
import com.itssvkv.todolist.ui.home.pager.PagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsHomeFragment : Fragment() {
    private var _binding: FragmentTabsHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTabsHomeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        initPagerAdapter()
    }

    private fun initPagerAdapter() {
        pagerAdapter = PagerAdapter(this)
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager){tab, position->
            when(position){
                0 -> {
                    tab.text = "Home"
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.home,
                        resources.newTheme()
                    )

                }
                1 -> {
                    tab.text = "Profile"
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.profile,
                        resources.newTheme()
                    )
                }
            }

        }.attach()
    }
}
