package com.itssvkv.todolist.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.itssvkv.todolist.databinding.FragmentWelcomeBinding
import com.itssvkv.todolist.ui.bottomsheet.ResultsBottomSheet
import com.itssvkv.todolist.ui.onboarding.pager.PagerAdapter


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var resultsBottomSheet : ResultsBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }
}
