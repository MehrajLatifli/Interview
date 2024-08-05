package com.example.interview.views.fragments.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.interview.R
import com.example.interview.databinding.FragmentHomeBinding
import com.example.interview.databinding.FragmentSessionCreateBinding
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionCreateFragment : BaseFragment<FragmentSessionCreateBinding>(FragmentSessionCreateBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // API_KEY=loadApiKey(requireContext()).toString()


    }
}