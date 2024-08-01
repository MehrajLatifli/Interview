package com.example.interview.views.fragments.vacancy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentVacancyCreateBinding
import com.example.interview.models.responses.get.position.PositionResponse
import com.example.interview.models.responses.get.structure.StructureResponse
import com.example.interview.models.responses.post.candidatedocument.CandidateDocument
import com.example.interview.models.responses.post.vacancy.Vacancy
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.candidate.CandidateCreateFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class VacancyCreateFragment : BaseFragment<FragmentVacancyCreateBinding>(FragmentVacancyCreateBinding::inflate){

    private val positionMap = mutableMapOf<String, Int>()
    private val structureMap = mutableMapOf<String, Int>()
    private lateinit var selectedPositionId: String
    private lateinit var selectedStructureId: String

    private val viewModel by viewModels<VacancyViewModel>()

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load drawable resource
        val dropdownBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.autocompletetextview_radiuscolor)

        binding.autocompletePositiontextview.setDropDownBackgroundDrawable(dropdownBackground)
        binding.autocompleteStructuretextview.setDropDownBackgroundDrawable(dropdownBackground)

        observeData()

        lifecycleScope.launch {
            val positions = viewModel.getAllPositions()
            positionMap.clear()
            positionMap.putAll(positions.associate { it.name to it.id })
            setupAutoCompleteTextView()

            val structures = viewModel.getAllStructures()
            structureMap.clear()
            structureMap.putAll(structures.associate { it.name to it.id })
            setupAutoCompleteTextView()
        }

        binding.buttonDateTime.setOnClickListener {
            showDatePicker()
        }

        binding.autocompletePositiontextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompletePositiontextview.adapter.getItem(position) as String
            selectedPositionId = positionMap[selectedItemName]?.toString() ?: ""
        }

        binding.autocompleteStructuretextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompleteStructuretextview.adapter.getItem(position) as String
            selectedStructureId = structureMap[selectedItemName]?.toString() ?: ""
        }

        binding.buttonCreate.setOnClickListener {
            val startDateTime = formatDateTimeforCalendar(calendar)

            val endDateCalendar = addMonthSafely(calendar)
            val endDateTime = formatDateTimeforCalendar(endDateCalendar)

            if(binding.editText.text.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Write title", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(binding.editText2.text.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Write description", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(binding.editText3.text.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Write date", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(startDateTime.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Write startDate", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(endDateTime.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Write endDate", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(selectedPositionId.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Select position", R.color.MellowMelon)
                return@setOnClickListener
            }

            if(selectedStructureId.isNullOrBlank()) {
                customresultdialog("Unsuccessful!", "Select structure", R.color.MellowMelon)
                return@setOnClickListener
            }

            val vacancy = Vacancy(
                title = binding.editText.text.toString(),
                description = binding.editText2.text.toString(),
                startDate = startDateTime,
                endDate = endDateTime,
                positionId = selectedPositionId.toIntOrNull() ?: 0,
                structureId = selectedStructureId.toIntOrNull() ?: 0
            )

            viewModel.addVacancy(vacancy)
        }
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                lifecycleScope.launch {
                    delay(2000)
                    binding.includeProgressbar.progressBar.visible()
                    binding.mainConstraintLayout.gone()
                    binding.NestedScrollView.gone()
                }
            } else {
                lifecycleScope.launch {
                    delay(2000)
                    binding.includeProgressbar.progressBar.gone()
                    binding.mainConstraintLayout.visible()
                    binding.NestedScrollView.visible()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                Log.e("VacancyCreate", errorMessage)
                customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
            }
        }

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            positionMap.clear()
            positionMap.putAll(positions.associate { it.name to it.id })
            setupAutoCompleteTextView()
        }

        viewModel.structures.observe(viewLifecycleOwner) { structures ->
            structureMap.clear()
            structureMap.putAll(structures.associate { it.name to it.id })
            setupAutoCompleteTextView()
        }

        viewModel.completeResult.observe(viewLifecycleOwner) { complateResult ->
            lifecycleScope.launch {
                if (complateResult) {
                    customresultdialog(
                        "Successful!",
                        "Please wait a moment, we are preparing for you...",
                        R.color.DeepPurple
                    )
                    delay(2500)
                    findNavController().navigate(VacancyCreateFragmentDirections.actionVacancyCreateFragmentToVacancyReadFragment())
                } else {
                    delay(2500)
                    findNavController().navigate(VacancyCreateFragmentDirections.actionVacancyCreateFragmentToOperationFragment())
                }
            }
        }
    }

    private fun addMonthSafely(startCalendar: Calendar): Calendar {
        val endCalendar = startCalendar.clone() as Calendar
        endCalendar.add(Calendar.MONTH, 1)

        val startDay = startCalendar.get(Calendar.DAY_OF_MONTH)
        val lastDayOfMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        if (startDay > lastDayOfMonth) {
            endCalendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth)
        }

        return endCalendar
    }

    private fun customresultdialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
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

    private fun setupAutoCompleteTextView() {
        val positionAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, positionMap.keys.toList())
        binding.autocompletePositiontextview.setAdapter(positionAdapter)

        val structureAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, structureMap.keys.toList())
        binding.autocompleteStructuretextview.setAdapter(structureAdapter)
    }

    private fun formatDateTimeforCalendar(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                lifecycleScope.launch {
                    delay(500)
                    showTimePicker()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialogTheme,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val dateTime = formatDateTimeforCalendar(calendar)
                binding.editText3.setText(formatDateTime(dateTime))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    fun formatDateTime(dateTimeString: String): String {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm:ss.SSS")

        val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
        return dateTime.format(outputFormatter)
    }
}

