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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class VacancyCreateFragment : BaseFragment<FragmentVacancyCreateBinding>(FragmentVacancyCreateBinding::inflate){

    private val positionList = mutableListOf<String>()
    private val structureList = mutableListOf<String>()
    private lateinit var selectedItem1: String
    private lateinit var selectedItem2: String

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


        obseveData()

        lifecycleScope.launch {
            viewModel.getAllPositions().let { positions ->
                positionList.clear()
                positionList.addAll(positions.map { it.name })
                setupAutoCompleteTextView()
            }

            viewModel.getAllStructures().let { structures ->
                structureList.clear()
                structureList.addAll(structures.map { it.name })
                setupAutoCompleteTextView()
            }
        }



        binding.buttonDateTime.setOnClickListener {
            showDatePicker()
        }

        binding.autocompletePositiontextview.setOnItemClickListener { _, _, position, _ ->
            selectedItem1= position.toString()
            // Handle item selection
        }

        binding.autocompleteStructuretextview.setOnItemClickListener { _, _, position, _ ->
            selectedItem2 = position.toString()
        }

        binding.buttonCreate.setOnClickListener {
            val startDateTime = formatDateTime(calendar)

            val endDateCalendar = addMonthSafely(calendar)
            val endDateTime = formatDateTime(endDateCalendar)

            val vacancy = Vacancy(
                title = binding.editText.text.toString(),
                description = binding.editText2.text.toString(),
                startDate = startDateTime,
                endDate = endDateTime,
                positionId = selectedItem1.toIntOrNull() ?: 0,
                structureId = selectedItem2.toIntOrNull() ?: 0
            )

            viewModel.addVacancy(vacancy)
        }

    }

    private fun obseveData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                lifecycleScope.launch {
                    delay(2000)
                    //  binding.includeBottomnav.BottomNavigationView.visible()
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
            positionList.clear()
            positionList.addAll(positions.map { it.name })
            setupAutoCompleteTextView()
        }

        viewModel.structures.observe(viewLifecycleOwner) { structures ->
            structureList.clear()
            structureList.addAll(structures.map { it.name })
            setupAutoCompleteTextView()
        }

    }
    private fun addMonthSafely(startCalendar: Calendar): Calendar {
        val endCalendar = startCalendar.clone() as Calendar
        endCalendar.add(Calendar.MONTH, 1)

        // Handle end-of-month scenario
        val startDay = startCalendar.get(Calendar.DAY_OF_MONTH)
        val lastDayOfMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        if (startDay > lastDayOfMonth) {
            endCalendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth)
        }

        return endCalendar
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


    private fun setupAutoCompleteTextView() {
        val positionAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, positionList)
        binding.autocompletePositiontextview.setAdapter(positionAdapter)

        val structureAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, structureList)
        binding.autocompleteStructuretextview.setAdapter(structureAdapter)
    }


    private fun formatDateTime(calendar: Calendar): String {
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
                val dateTime = formatDateTime(calendar)
                binding.editText3.setText(dateTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

}
