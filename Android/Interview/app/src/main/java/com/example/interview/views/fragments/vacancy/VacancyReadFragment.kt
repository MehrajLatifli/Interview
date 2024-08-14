package com.example.interview.views.fragments.vacancy

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentCandidateReadBinding
import com.example.interview.databinding.FragmentVacancyReadBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.adapters.candidate.CandidateAdapter
import com.example.interview.views.adapters.vacancy.VacancyAdapter
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.candidate.CandidateReadFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VacancyReadFragment : BaseFragment<FragmentVacancyReadBinding>(FragmentVacancyReadBinding::inflate) {


    private val viewModel by viewModels<VacancyViewModel>()

    var vacancyAdapter = VacancyAdapter()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.let { bitem ->
            bitem?.rvVacancies?.adapter = vacancyAdapter

            viewModel?.getAllVacancies()


            swipeRefreshLayout = bitem.swipeRefreshLayout
            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.ComingUpRoses
                )
            )

            swipeRefreshLayout.setOnRefreshListener {

                viewModel.getAllVacancies()
                swipeRefreshLayout.isRefreshing = false
            }

            observeData()


            vacancyAdapter.onClickDeleteItem = { id ->

                val position = vacancyAdapter.list.indexOfFirst { it.id == id }

                if (position != -1) {
                    vacancyAdapter.deleteItem(position)
                    viewModel?.deleteVacancyById(id)
                }


            }

            vacancyAdapter.onClickDetailItem = { vacancy ->


                findNavController().navigate(
                    VacancyReadFragmentDirections.actionVacancyReadFragmentToVacancyDetailFragment(
                        vacancy
                    )
                )
            }


            vacancyAdapter.onClickUpdateItem = { vacancy ->


                findNavController().navigate(
                    VacancyReadFragmentDirections.actionVacancyReadFragmentToVacancyUpdateFragment(
                        vacancy
                    )
                )
            }

        }

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())

    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        binding?.let { bitem ->


            lifecycleScope.launch {
                vacancyAdapter?.setFontSizes(savedPrimaryFontSize, savedSecondaryFontSize)
            }


        }



    }

    private fun getPrimaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sp.getFloat("primaryFontsize", 16.0F)
    }

    private fun getSecondaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)

        return sp.getFloat("secondaryFontsize", 12.0F)
    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (themeName == "Secondary") {
                binding.Main.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
                binding.NestedScrollView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {



        viewModel?.vacancies?.observe(viewLifecycleOwner) { item ->
            if (isAdded) {
                lifecycleScope.launch {
                    if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        binding?.let { bitem ->
                            delay(500)
                            vacancyAdapter.updateList(item)


                            val layoutAnimationController =
                                AnimationUtils.loadLayoutAnimation(
                                    context,
                                    R.anim.item_layout_animation
                                )
                            bitem?.rvVacancies?.layoutAnimation = layoutAnimationController
                        }

                    }
                }
            }



        }


        viewModel?.loading?.observe(viewLifecycleOwner) { isLoading ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            binding?.let { bitem ->
                if (isLoading) {

                    bitem?.includeProgressbar?.progressBar?.visible()
                    bitem?.NestedScrollView?.gone()


//                if(viewModel.getAllCandidateDocuments().isEmpty())
//                {
//                    findNavController().navigate(R.id.action_candidateReadFragment_to_operationFragment)
//
//                }

                } else {
                    bitem?.includeProgressbar?.progressBar?.gone()
                    bitem?.NestedScrollView?.visible()
                }
            }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (!errorMessage.isNullOrBlank()) {
                    Log.e("CandidateViewModel", errorMessage)
                    customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)



                    if (viewModel?.getAllVacancies()?.size ?: 0 <= 0) {
                        findNavController().navigate(CandidateReadFragmentDirections.actionCandidateReadFragmentToCandidateCreateFragment())
                    }
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