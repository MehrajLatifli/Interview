package com.example.interview.views.fragments.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.interview.R
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.databinding.FragmentSessionUpdateBinding
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionUpdateFragment : BaseFragment<FragmentSessionUpdateBinding>(FragmentSessionUpdateBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // API_KEY=loadApiKey(requireContext()).toString()


    }
}