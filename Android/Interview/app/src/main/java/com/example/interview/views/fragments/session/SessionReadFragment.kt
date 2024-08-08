package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.models.responses.post.sessionquestion.RandomQuestionRequest2
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.views.adapters.session.SessionAdapder
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@AndroidEntryPoint
class SessionReadFragment : BaseFragment<FragmentSessionReadBinding>(FragmentSessionReadBinding::inflate) {

    private val viewModel by viewModels<SessionViewModel>()
    private val sessionAdapder = SessionAdapder()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var size: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvSessions.adapter = sessionAdapder
        observeData()


        size=viewModel.getAllOwnSession().size



        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.DeepPurple)
        )
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllOwnSession()
            swipeRefreshLayout.isRefreshing = false
        }



        sessionAdapder.onClickDeleteItem = { id ->
            val position = sessionAdapder.list.indexOfFirst { it.id == id }
            if (id != null && position != -1) {
                sessionAdapder.deleteItem(position)
                viewModel.deleteSessionById(id)
            }
        }

        sessionAdapder.onClickDetailItem = { session ->
            findNavController().navigate(
                SessionReadFragmentDirections.actionSessionReadFragmentToSessionDetailFragment(session)
            )
        }

        sessionAdapder.onClickStartExemItem = { session ->
            Log.d("SessionReadFragment", "Adding new questions for sessionId: ${session.id}, vacancyId: ${session.vacancyId}")

            viewModel.getSessionQuestionBySessionId(session.id!!)

            viewModel.sessionquestions.observe(viewLifecycleOwner) { questions ->
                lifecycleScope.launch {
                    if (questions.isEmpty()) {

                        viewModel.addRandomSessionQuestion(
                            10,
                            session.vacancyId!!,
                            session.id!!
                        )

                        delay(2000)

                        findNavController().navigate(
                            SessionReadFragmentDirections.actionSessionReadFragmentToSessionUpdateFragment(session)
                        )

                        Toast.makeText(
                            requireContext(),
                            "Added questions for this session",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        delay(1000)
                        findNavController().navigate(
                            SessionReadFragmentDirections.actionSessionReadFragmentToSessionUpdateFragment(session)
                        )
                    }
                }
            }
        }





        binding.createButton.setOnClickListener {
            findNavController().navigate(SessionReadFragmentDirections.actionSessionReadFragmentToSessionCreateFragment())
        }
    }


    override fun onResume() {
        super.onResume()

        viewModel.getAllOwnSession()


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
            lifecycleScope.launch {
                if (isLoading) {
                    binding.includeProgressbar.progressBar.visible()
                    binding.NestedScrollView.gone()
                    binding.createButton.gone()
                    delay(1000)
                } else {
                    delay(1000)
                    binding.includeProgressbar.progressBar.gone()
                    binding.NestedScrollView.visible()
                    binding.createButton.visible()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank() && sessionAdapder.list.isNotEmpty()) {

                if (size>0) {
                    Log.e("SessionViewModel", errorMessage)
                    customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
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
