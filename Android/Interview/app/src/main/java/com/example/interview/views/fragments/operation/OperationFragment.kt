package com.example.interview.views.fragments.operation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.FragmentOperationBinding
import com.example.interview.models.localadapdermodels.operationcrud.Operation
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.utilities.gone
import com.example.interview.views.adapters.operationtype.OperationTypeAdapder
import com.example.interview.views.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OperationFragment : BaseFragment<FragmentOperationBinding>(
    FragmentOperationBinding::inflate) {

    val operations = arrayListOf(
        Operation("Create", R.drawable.create),
        Operation("Read", R.drawable.read),
//        OperationCRUD("Update", R.drawable.update),
//        OperationCRUD("Delete", R.drawable.delete)
    )

    private var operationTypeList = arrayListOf(
        OperationType(R.drawable.candidate, "Candidate", operations),
        OperationType(R.drawable.vacancy, "Vacancy", operations),
        OperationType(R.drawable.question, "Question", operations),
//        OperationType(R.drawable.category, "Category", operationCRUD),
//        OperationType(R.drawable.structure, "Structure", operationCRUD),
//        OperationType(R.drawable.level, "Level", operationCRUD),
//        OperationType(R.drawable.position, "Position", operationCRUD),

    )

    private val operationTypeAdapder = OperationTypeAdapder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.includeProgressbar.progressBar.gone()

        operationTypeAdapder.itemClickHandler = { selectedItemText, operationTypeText ->
            lifecycleScope.launch(Dispatchers.Main) {
                if (isAdded && !isStateSaved) {
                    delay(300)
                    if (operationTypeText == "Candidate" && selectedItemText == "Create") {
                        val action = OperationFragmentDirections.actionOperationFragmentToCandidateCreateFragment()
                        findNavController().navigate(action)
                    }
                    if (operationTypeText == "Candidate" && selectedItemText == "Read") {
                        val action = OperationFragmentDirections.actionOperationFragmentToCandidateReadFragment()
                        findNavController().navigate(action)
                    }
                }
                if (operationTypeText == "Vacancy" && selectedItemText == "Create") {
                    val action = OperationFragmentDirections.actionOperationFragmentToVacancyCreateFragment()
                    findNavController().navigate(action)
                }
                if (operationTypeText == "Vacancy" && selectedItemText == "Read") {
//                    val action = OperationFragmentDirections.actionOperationFragmentToCandidateReadFragment()
//                    findNavController().navigate(action)
                }
            }
        }

        binding.rvOperationtype.adapter = operationTypeAdapder
        operationTypeAdapder.updateList(operationTypeList)
    }
}
