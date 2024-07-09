package com.example.interview.views.fragments.accounttype

import android.os.Bundle
import android.view.View
import com.example.interview.R
import com.example.interview.databinding.FragmentAccountTypeBinding
import com.example.interview.models.localadapdermodels.accounttype.AccountType
import com.example.interview.views.adapters.accounttype.AccountTypeAdapder

import com.example.interview.views.fragments.base.BaseFragment


class AccountTypeFragment  : BaseFragment<FragmentAccountTypeBinding>(FragmentAccountTypeBinding::inflate) {


    private var accountTypeList = arrayListOf(
        AccountType(R.drawable.person_1, "Admin"),
        AccountType(R.drawable.person_2, "HR"),
        AccountType(R.drawable.person_3, "Custom")

    )

    private val accountTypeAdapder = AccountTypeAdapder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvAccounttype.adapter = accountTypeAdapder

        accountTypeAdapder.updateList(accountTypeList)

    }
}