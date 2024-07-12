package com.example.interview.views.fragments.walkthrough

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.FragmentWalkthroughBinding
import com.example.interview.models.localadapdermodels.walkthrough.Walkthrough
import com.example.interview.views.adapters.walkthrough.WalkthroughAdapter
import com.example.interview.views.fragments.base.BaseFragment


class WalkthroughFragment : BaseFragment<FragmentWalkthroughBinding>(FragmentWalkthroughBinding::inflate) {


    private var walkthroughList = arrayListOf(
        Walkthrough(R.drawable.walkthrough_1, "Create, share and play quizzes whenever and wherever you want"),
        Walkthrough(R.drawable.walkthrough_2, "Find fun and interesting quizzes to boost up your knowledge"),
        Walkthrough(R.drawable.walkthrough_3, "Play and take quiz challenges together with your friends.")
    )

    private val walkthroughAdapter = WalkthroughAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        walkthroughAdapter.updateList(walkthroughList)


        binding.viewPager2.adapter = walkthroughAdapter


        val dotsIndicator = binding.indicator
        val viewPager = binding.viewPager2
        viewPager.adapter = walkthroughAdapter
        dotsIndicator.attachTo(viewPager)


        binding.RegistrationButton.setOnClickListener{

            findNavController().navigate(WalkthroughFragmentDirections.actionWalkthroughFragmentToAccountTypeFragment())
        }

        binding.LogInButton.setOnClickListener{

            findNavController().navigate(WalkthroughFragmentDirections.actionWalkthroughFragmentToLogInFragment())
        }




    }
}