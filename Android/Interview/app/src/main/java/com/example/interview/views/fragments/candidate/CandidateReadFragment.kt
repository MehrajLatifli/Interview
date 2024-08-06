package com.example.interview.views.fragments.candidate

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentCandidateReadBinding
import com.example.interview.databinding.FragmentHomeBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.views.adapters.candidate.CandidateAdapter
import com.example.interview.views.fragments.auth.login.LogInFragmentDirections
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CandidateReadFragment  : BaseFragment<FragmentCandidateReadBinding>(FragmentCandidateReadBinding::inflate) {

    private val viewModel by viewModels<CandidateViewModel>()

    var candidateAdapter = CandidateAdapter()


    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAllCandidateDocuments()

        observeData()


        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.ThickBlue))

        swipeRefreshLayout.setOnRefreshListener {

            viewModel.getAllCandidateDocuments()
            swipeRefreshLayout.isRefreshing = false
        }




        binding.rvCandidates.adapter=candidateAdapter



        candidateAdapter.onClickDeleteItem = { id ->

            val position = candidateAdapter.list.indexOfFirst { it.id == id }

            if (position != -1) {
                candidateAdapter.deleteItem(position)
                viewModel.deleteCandidateDocumentById(id)
            }





        }

        candidateAdapter.onClickDetailItem = { id ->


            findNavController().navigate(CandidateReadFragmentDirections.actionCandidateReadFragmentToCandidateDetailFragment(id))
        }


        candidateAdapter.onClickUpdateItem = { id ->


            findNavController().navigate(CandidateReadFragmentDirections.actionCandidateReadFragmentToCandidateUpdateFragment(id))
        }









    }


    private fun observeData() {



        viewModel.candidateDocuments.observe(viewLifecycleOwner) { item ->


                candidateAdapter.updateList(item)


            val layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.item_layout_animation)
            binding.rvCandidates.layoutAnimation = layoutAnimationController




        }


        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                binding.includeProgressbar.progressBar.visible()
                binding.NestedScrollView.gone()


//                if(viewModel.getAllCandidateDocuments().isEmpty())
//                {
//                    findNavController().navigate(R.id.action_candidateReadFragment_to_operationFragment)
//
//                }

            } else {
                binding.includeProgressbar.progressBar.gone()
                binding.NestedScrollView.visible()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                Log.e("CandidateViewModel", errorMessage)
                customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)



                if (viewModel.getAllCandidateDocuments().size <= 0) {
                    findNavController().navigate(CandidateReadFragmentDirections.actionCandidateReadFragmentToCandidateCreateFragment())
                }
            }

        }

        viewModel.afterdeleteResult.observe(viewLifecycleOwner) { isafterdeleteResult ->
            if (isafterdeleteResult) {



            }
        }
    }

    private fun customresultdialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomresultdialogBinding.inflate(LayoutInflater.from(requireContext()))
            val dialog = AlertDialog.Builder(requireContext()).apply {
                setView(dialogBinding.root)
            }.create()

            dialogBinding.itemimageView.loadImageWithGlideAndResize(R.drawable.registerstatuse, requireContext())
            dialogBinding.titleTextView.text = title
            dialogBinding.titleTextView.setTextColor(ContextCompat.getColor(requireContext(), colorId))
            dialogBinding.textTextView.text = text
            dialogBinding.textTextView.setTextColor(ContextCompat.getColor(requireContext(), colorId))

            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }


}