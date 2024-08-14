package com.example.interview.views.fragments.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentHomeBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.home.HomeViewModel
import com.example.interview.views.adapters.candidate.CandidateAdapter
import com.example.interview.views.adapters.session.SessionAdapder
import com.example.interview.views.adapters.vacancy.VacancyAdapter
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private val vacancyAdapter = VacancyAdapter()
    private val candidateAdapter = CandidateAdapter()
    private val sessionAdapder = SessionAdapder()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var size1: Int = 0
    private var size2: Int = 0
    private var size3: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { bitem ->
            // Initialize adapters with visibility settings
            vacancyAdapter.apply {
                setDeleteButtonVisibility(false)
                setDetailButtonVisibility(false)
                setUpdateButtonVisibility(false)
            }

            candidateAdapter.apply {
                setDeleteButtonVisibility(false)
                setDetailButtonVisibility(false)
                setUpdateButtonVisibility(false)
            }

            sessionAdapder.apply {
                setDeleteButtonVisibility(false)
                setDetailButtonVisibility(false)
                setStartExamButtonVisibility(false)
            }

            size1 = viewModel.getAllVacancies().size
            size2 = viewModel.getAllCandidateDocuments().size
            size3 = viewModel.getAllOwnSession().size




            if (getThemeName() == "Secondary") {
                activity?.window?.let { window ->
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = false
                        isAppearanceLightNavigationBars = false
                    }
                    window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Black)
                    window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.DeepPurple)
                }
            }






            bitem.rvVacancies.adapter = vacancyAdapter
            bitem.rvCandidates.adapter = candidateAdapter
            bitem.rvSessions.adapter = sessionAdapder


            swipeRefreshLayout = bitem.swipeRefreshLayout
            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.DeepPurple)
            )
            swipeRefreshLayout.setOnRefreshListener {
                refreshData()
                swipeRefreshLayout.isRefreshing = false
            }

            observeData()
            val themeName = getThemeName() ?: "Primary"
            applyTheme(themeName)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.vacancies.removeObservers(viewLifecycleOwner)
        viewModel.sessions.removeObservers(viewLifecycleOwner)
        viewModel.candidateDocuments.removeObservers(viewLifecycleOwner)
    }

    private fun applyTheme(themeName: String) {
        if (themeName == "Secondary") {
            binding?.apply {
                Main.background = ContextCompat.getDrawable(requireContext(), R.color.Black)
                rvVacancies.background = ContextCompat.getDrawable(requireContext(), R.color.Black)
                rvCandidates.background = ContextCompat.getDrawable(requireContext(), R.color.Black)
            }
        }
    }

    private fun observeData() {
        viewModel.vacancies.observe(viewLifecycleOwner) { items ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    vacancyAdapter.updateList(items)
                    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                        requireContext(),
                        R.anim.item_layout_animation
                    )
                    bitem.rvVacancies.layoutAnimation = layoutAnimationController




                }
            }
        }

        viewModel.candidateDocuments.observe(viewLifecycleOwner) { items ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    candidateAdapter.updateList(items)
                    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                        requireContext(),
                        R.anim.item_layout_animation
                    )
                    bitem.rvCandidates.layoutAnimation = layoutAnimationController


                }
            }
        }

        viewModel.sessions.observe(viewLifecycleOwner) { items ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    sessionAdapder.updateList(items)
                    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                        requireContext(),
                        R.anim.item_layout_animation
                    )
                    bitem.rvSessions.layoutAnimation = layoutAnimationController



                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    if (isLoading) {
                        bitem.includeProgressbar.progressBar.visible()
                        bitem.rvVacancies.gone()
                        bitem.rvCandidates.gone()
                    } else {
                        bitem.includeProgressbar.progressBar.gone()
                        bitem.rvVacancies.visible()
                        bitem.rvCandidates.visible()
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (!errorMessage.isNullOrBlank() && vacancyAdapter.list.isNotEmpty() && candidateAdapter.list.isNotEmpty()) {
                    if (size1 > 0 && size2 > 0 && size3 > 0) {
                        Log.e("HomeViewModel", errorMessage)
                        showCustomResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                    }
                }
            }
        }
    }

    private fun refreshData() {
        viewModel.getAllVacancies()
        viewModel.getAllCandidateDocuments()
        viewModel.getAllOwnSession()
    }

    private fun showCustomResultDialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch {
            val dialogBinding = CustomresultdialogBinding.inflate(LayoutInflater.from(requireContext()))
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

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }
}
