package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.utilities.NetworkChangeReceiver
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.views.adapters.session.SessionAdapder
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock


@AndroidEntryPoint
class SessionReadFragment : BaseFragment<FragmentSessionReadBinding>(FragmentSessionReadBinding::inflate) {

    private val viewModel by viewModels<SessionViewModel>()
    private val sessionAdapder = SessionAdapder()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var size: Int = 0
    private val bindingLock = ReentrantLock()
    private val bindingLock2 = ReentrantLock()

    private var searchJob: Job? = null

    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        if (isAdded && isVisible) {
            handleNetworkStatusChange(isConnected)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        binding.createButton.gone()


        binding?.let { bitem ->

            bitem?.rvSessions?.adapter = sessionAdapder


            observeData()


            size = viewModel?.getAllOwnSession()?.size ?: 0



            swipeRefreshLayout = bitem.swipeRefreshLayout
            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.DeepPurple)
            )

            swipeRefreshLayout.setOnRefreshListener {
                viewModel?.getAllOwnSession()
                swipeRefreshLayout.isRefreshing = false
            }




            sessionAdapder.onClickDeleteItem = { id ->
                val position = sessionAdapder.list.indexOfFirst { it.id == id }
                if (id != null && position != -1) {
                    sessionAdapder.deleteItem(position)
                    viewModel?.deleteSessionById(id)
                }
            }

            sessionAdapder.onClickDetailItem = { session ->
                findNavController().navigate(
                    SessionReadFragmentDirections.actionSessionReadFragmentToSessionDetailFragment(
                        session
                    )
                )
            }

            sessionAdapder.onClickStartExemItem = { session ->
                Log.d(
                    "SessionReadFragment",
                    "Adding new questions for sessionId: ${session.id}, vacancyId: ${session.vacancyId}"
                )

                viewModel.getSessionQuestionBySessionId(session.id!!)

                viewModel?.sessionquestions?.observe(viewLifecycleOwner) { questions ->
                    lifecycleScope.launch {
                        if (questions.isEmpty()) {

                            viewModel.addRandomSessionQuestion(
                                10,
                                session.vacancyId!!,
                                session.id!!
                            )

                            delay(2000)

                            findNavController().navigate(
                                SessionReadFragmentDirections.actionSessionReadFragmentToSessionUpdateFragment(
                                    session
                                )
                            )

                            Toast.makeText(
                                requireContext(),
                                "Added questions for this session",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            delay(1000)
                            findNavController().navigate(
                                SessionReadFragmentDirections.actionSessionReadFragmentToSessionUpdateFragment(
                                    session
                                )
                            )
                        }
                    }
                }
            }









            val themeName = getThemeName() ?: "Primary"
            applyTheme(themeName)


                applySize(getPrimaryFontsize(), getSecondaryFontsize())

        }
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(networkChangeReceiver)
        viewModel?.vacancies?.removeObservers(viewLifecycleOwner)
        viewModel?.sessions?.removeObservers(viewLifecycleOwner)
        viewModel?.profiles?.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    private fun handleNetworkStatusChange(isConnected: Boolean) {
        lifecycleScope.launch {
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    if (isConnected) {
                        bitem.NestedScrollView.visible()
                        observeData()




                        binding.createButton.setOnClickListener {
                            findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionCreateFragment())
                        }

                    } else {
                        sessionAdapder.updateList(emptyList())
                        bitem.createButton.gone()
                        bitem.NestedScrollView.gone()
                    }
                }
            }
        }
    }



    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        binding?.let { bitem ->


            lifecycleScope.launch {
                sessionAdapder?.setFontSizes(savedPrimaryFontSize, savedSecondaryFontSize)
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
                    R.color.Black
                )
                binding.NestedScrollView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.Black
                )
            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }


    override fun onResume() {
        super.onResume()



    }



    private fun observeData() {
        viewModel?.sessions?.observe(viewLifecycleOwner) { item ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    delay(500)
                    binding?.let { bitem ->
                        sessionAdapder.updateList(item)
                        val layoutAnimationController =
                            AnimationUtils.loadLayoutAnimation(
                                context,
                                R.anim.item_layout_animation
                            )
                        bitem.rvSessions.layoutAnimation = layoutAnimationController
                    }
                }
            }
        }

        viewModel?.loading?.observe(viewLifecycleOwner) { isLoading ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        if (isLoading) {
                            bitem.includeProgressbar.progressBar.visible()
                            bitem.NestedScrollView.gone()
                            bitem.createButton.gone()
                            delay(2000)
                        } else {
                            bitem.includeProgressbar.progressBar.gone()
                            bitem.NestedScrollView.visible()
                            bitem.createButton.visible()
                            delay(2000)
                        }
                    }
                }
            }
        }

        viewModel?.error?.observe(viewLifecycleOwner) { errorMessage ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    if (!errorMessage.isNullOrBlank() && sessionAdapder.list.isNotEmpty()) {
                        if (size > 0) {
                            Log.e("SessionViewModel", errorMessage)
                            customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                        }
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
