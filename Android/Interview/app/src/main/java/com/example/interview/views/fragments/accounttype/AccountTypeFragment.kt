package com.example.interview.views.fragments.accounttype

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.FragmentAccountTypeBinding
import com.example.interview.models.localadapdermodels.accounttype.AccountType
import com.example.interview.utilities.gone
import com.example.interview.utilities.visible
import com.example.interview.views.adapters.accounttype.AccountTypeAdapter
import com.example.interview.views.fragments.auth.login.LogInFragmentDirections

import com.example.interview.views.fragments.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AccountTypeFragment  : BaseFragment<FragmentAccountTypeBinding>(FragmentAccountTypeBinding::inflate) {


    private var accountTypeList = arrayListOf(
        AccountType(R.drawable.person_1, "Admin"),
        AccountType(R.drawable.person_2, "HR"),

    )

    private val accountTypeAdapder = AccountTypeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.includeProgressbar.progressBar.gone()



        binding.rvAccounttype.adapter = accountTypeAdapder

        accountTypeAdapder.updateList(accountTypeList)

        accountTypeAdapder.onClickItem = { accountType ->

            lifecycleScope.launch {

                binding.rvAccounttype.gone()
                binding.textView.gone()
                delay(200)

                binding.includeProgressbar.progressBar.visible()

                delay(2000)

                binding.includeProgressbar.progressBar.gone()

                val action =
                    AccountTypeFragmentDirections.actionAccountTypeFragmentToRegistrationFragment(
                        accountType
                    )
                findNavController().navigate(action)
            }
        }



    }


}