package com.example.interview.views.fragments.operation


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentOperationBinding
import com.example.interview.models.localadapdermodels.operationcrud.OperationCRUD
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.views.adapters.operationcrud.OperationCRUDAdapter
import com.example.interview.views.adapters.operationtype.OperationTypeAdapder
import com.example.interview.views.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OperationFragment : BaseFragment<FragmentOperationBinding>(
    FragmentOperationBinding::inflate) {


    val operationCRUD = arrayListOf(
        OperationCRUD("Create", R.drawable.create),
        OperationCRUD("Read", R.drawable.read),
        OperationCRUD("Update", R.drawable.update),
        OperationCRUD("Delete", R.drawable.delete)
    )


    private var operationTypeList = arrayListOf(
        OperationType(R.drawable.candidate, "Candidate", operationCRUD),
        OperationType(R.drawable.category, "Category", operationCRUD),
        OperationType(R.drawable.structure, "Structure", operationCRUD),
        OperationType(R.drawable.level, "Level", operationCRUD),
        OperationType(R.drawable.position, "Position", operationCRUD),
        OperationType(R.drawable.vacancy, "Vacancy", operationCRUD),
        OperationType(R.drawable.question, "Question", operationCRUD),

        )

    private val operationTypeAdapder = OperationTypeAdapder()

    private val operationCRUDAdapter = OperationCRUDAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.includeProgressbar.progressBar.gone()


        operationTypeAdapder


        binding.rvOperationtype.adapter = operationTypeAdapder

        operationTypeAdapder.updateList(operationTypeList)


        if(getUserAuth()==true || !loadApiKey().isNullOrBlank())
        {

        }

    }

    private fun getUserAuth(): Boolean {
        val sp = requireActivity().getSharedPreferences("authresult_local", Context.MODE_PRIVATE)
        return sp.getBoolean("isAuth", false)
    }

    private fun loadApiKey(): String? {
        // Use requireContext() to obtain the context safely within the Fragment
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("api_key", null)
    }


}