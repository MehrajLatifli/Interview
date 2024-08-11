package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.databinding.FragmentSessionUpdateBinding
import com.example.interview.models.localadapdermodels.operationcrud.Operation
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.models.localadapdermodels.question.Question
import com.example.interview.models.localadapdermodels.questionvalue.QuestionValue
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.models.responses.get.sessionquestion.SessionQuestionResponse
import com.example.interview.models.responses.post.session.SessionRequest
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.views.adapters.operationtype.OperationTypeAdapder
import com.example.interview.views.adapters.question.QuestionAdapter
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.operation.OperationFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class SessionUpdateFragment : BaseFragment<FragmentSessionUpdateBinding>(FragmentSessionUpdateBinding::inflate) {

    private val viewModel by viewModels<SessionViewModel>()
    private val questionAdapter = QuestionAdapter()

    private val args: SessionUpdateFragmentArgs by navArgs()
    private var sessionQuestionId: Int = 0

    val questionValues = arrayListOf(
        QuestionValue(1),
        QuestionValue(2),
        QuestionValue(3),
        QuestionValue(4),
        QuestionValue(5)
    )

    var questionList = emptyList<Question>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSessionQuestionBySessionId(args.session.id!!.toInt())
        observeData()

        binding.includeProgressbar.progressBar.gone()

        questionAdapter.itemClickHandler = { selectedItemText, sessionQuestionId ->
            Log.d("SessionUpdateFragment2", "Item clicked: Question ID=$sessionQuestionId, SelectedText=$selectedItemText")

            lifecycleScope.launch {
                val sessionQuestion = questionList.find { it.id == sessionQuestionId }
                sessionQuestion?.let {
                    this@SessionUpdateFragment.sessionQuestionId = it.sessionQuestionid
                    val updatedSessionQuestion = SessionQuestionResponse(
                        id = this@SessionUpdateFragment.sessionQuestionId,
                        value = selectedItemText.toInt(),
                        sessionId = args.session.id?.toInt(),
                        questionId = it.id
                    )
                    Log.d(
                        "SessionUpdateFragment2",
                        "Updating session question with ID: ${updatedSessionQuestion.toString()}"
                    )
                    delay(200)
                    viewModel.updateSessionQuestion(updatedSessionQuestion)
                }
            }
        }

        binding.rvQuestion.adapter = questionAdapter

        binding.buttonFinishExam.setOnClickListener {


            lifecycleScope.launch {
                val sessionResponse = SessionResponse(
                    id = args.session.id,
                    endValue = args.session.endValue,
                    startDate = LocalDateTime.now().toString(),
                    endDate = LocalDateTime.now().toString(),
                    vacancyId = args.session.vacancyId?.toInt(),
                    candidateId = args.session.candidateId?.toInt(),
                    userId = args.session.userId?.toInt()
                )



                viewModel.updateSession(sessionResponse)

                delay(1000)

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.sessionUpdateFragment, true)
                    .setLaunchSingleTop(true)
                    .build()

                findNavController().navigate(SessionUpdateFragmentDirections.actionSessionUpdateFragmentToSessionReadFragment(),navOptions)
            }

        }
        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    override fun onDestroyView() {
        viewModel?.vacancies?.removeObservers(viewLifecycleOwner)
        viewModel?.sessions?.removeObservers(viewLifecycleOwner)
        viewModel?.profiles?.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
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

    private fun applySize(primaryFontSize: Float, secondaryFontSize: Float) {
        binding?.let {
            questionAdapter.setFontSizes(primaryFontSize, secondaryFontSize)
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

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {
        viewModel.sessionquestions.observe(viewLifecycleOwner) { sessionQuestions ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Log.d("SessionUpdateFragment", "Session questions updated: $sessionQuestions")
            lifecycleScope.launch {
                val updatedQuestions = mutableListOf<Question>()
                for (sessionQuestion in sessionQuestions) {
                    sessionQuestion.questionId?.let { id ->
                        viewModel.getQuestionByID(id.toInt())
                            .observe(viewLifecycleOwner) { question ->
                                question?.let {
                                    Log.d("SessionUpdateFragment", "Mapping question ID: ${it.id}")
                                    val mappedQuestion = Question(
                                        it.id!!,
                                        sessionQuestion.id!!,
                                        R.drawable.question,
                                        it.text.toString(),
                                        questionValues
                                    )
                                    updatedQuestions.add(mappedQuestion)
                                    questionList = updatedQuestions
                                    questionAdapter.updateList(questionList)

                                    // Restore the selected item state
                                    val selectedItem =
                                        sessionQuestions.find { sq -> sq.questionId == it.id }?.value?.toString()
                                    questionAdapter.setSelectedItem(it.id, selectedItem.toString())
                                }
                            }
                    }
                }
            }
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (!errorMessage.isNullOrBlank() && questionAdapter.list.isNotEmpty()) {
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







