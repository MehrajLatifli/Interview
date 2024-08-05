package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionDetailBinding
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.adapters.session.SessionAdapder
import com.example.interview.views.adapters.vacancy.VacancyAdapter
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.candidate.CandidateReadFragmentDirections
import com.example.interview.views.fragments.vacancy.VacancyReadFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SessionReadFragment : BaseFragment<FragmentSessionReadBinding>(FragmentSessionReadBinding::inflate) {

    private val viewModel by viewModels<SessionViewModel>()
    var sessionAdapder = SessionAdapder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSessions.adapter=sessionAdapder

        observeData()

        viewModel.getAllOwnSession()

        sessionAdapder.onClickDeleteItem = { id ->

            val position = sessionAdapder.list.indexOfFirst { it.id == id }

            if (id != null) {
                if (position != -1) {
                    sessionAdapder.deleteItem(position)
                    viewModel.deleteSessionById(id)
                }
            }
        }

        sessionAdapder.onClickDetailItem = { session ->


            findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionDetailFragment(session))
        }

        sessionAdapder.onClickStartExemItem = { session ->

            findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionUpdateFragment(session))
        }

        binding.createButton.setOnClickListener {

            findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionCreateFragment())
        }

    }

    private fun observeData() {



        viewModel.sessions.observe(viewLifecycleOwner) { item ->

            lifecycleScope.launch {
                delay(500)
                sessionAdapder.updateList(item)


                val layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.item_layout_animation)
                binding.rvSessions.layoutAnimation = layoutAnimationController

            }



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



                if (viewModel.getAllOwnSession().size <= 0) {
                    findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionCreateFragment())
                }
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