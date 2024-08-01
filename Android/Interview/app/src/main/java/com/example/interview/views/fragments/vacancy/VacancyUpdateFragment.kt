package com.example.interview.views.fragments.vacancy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentVacancyUpdateBinding
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.vacancy.Vacancy
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.fragments.base.BaseFragment
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
class VacancyUpdateFragment : BaseFragment<FragmentVacancyUpdateBinding>(FragmentVacancyUpdateBinding::inflate) {

    private val args: VacancyUpdateFragmentArgs by navArgs()
    private val viewModel by viewModels<VacancyViewModel>()

    private val positionMap = mutableMapOf<String, Int>()
    private val structureMap = mutableMapOf<String, Int>()
    private var selectedPositionId: Int? = null
    private var selectedStructureId: Int? = null

    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVacancyByID(args.vacancy.id)
        viewModel.getPositionByID(args.vacancy.positionId)
        viewModel.getStructureByID(args.vacancy.structureId)

        observeData()

        lifecycleScope.launch {
            val positions = viewModel.getAllPositions()
            positions.forEach { position -> positionMap[position.name] = position.id }
            setupAutoCompleteTextView()

            val structures = viewModel.getAllStructures()
            structures.forEach { structure -> structureMap[structure.name] = structure.id }
            setupAutoCompleteTextView()
        }

        binding.autocompletePositiontextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompletePositiontextview.adapter.getItem(position) as String
            selectedPositionId = positionMap[selectedItemName]
        }

        binding.autocompleteStructuretextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompleteStructuretextview.adapter.getItem(position) as String
            selectedStructureId = structureMap[selectedItemName]
        }

        binding.buttonUpdate.setOnClickListener {
            val startDateTime = formatDateTimeforCalendar(calendar)
            val endDateTime = formatDateTimeforCalendar(addMonthSafely(calendar))

            if (binding.editText.text.isNullOrBlank()) {
                customResultDialog("Unsuccessful!", "Write title", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (binding.editText2.text.isNullOrBlank()) {
                customResultDialog("Unsuccessful!", "Write description", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (binding.editText3.text.isNullOrBlank()) {
                customResultDialog("Unsuccessful!", "Write date", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (startDateTime.isNullOrBlank()) {
                customResultDialog("Unsuccessful!", "Write startDate", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (endDateTime.isNullOrBlank()) {
                customResultDialog("Unsuccessful!", "Write endDate", R.color.MellowMelon)
                return@setOnClickListener
            }

            val vacancy = VacancyResponse(
                id = args.vacancy.id,
                title = binding.editText.text.toString(),
                description = binding.editText2.text.toString(),
                startDate = startDateTime,
                endDate = endDateTime,
                positionId = selectedPositionId ?: 0,
                structureId = selectedStructureId ?: 0
            )

            viewModel.updateVacancy(vacancy)
        }
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            lifecycleScope.launch {
                delay(2000)
                if (isLoading) {
                    binding.includeProgressbar.progressBar.visible()
                    binding.mainConstraintLayout.gone()
                    binding.NestedScrollView.gone()
                } else {
                    binding.includeProgressbar.progressBar.gone()
                    binding.mainConstraintLayout.visible()
                    binding.NestedScrollView.visible()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                Log.e("VacancyUpdate", errorMessage)
                customResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
            }
        }

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            positionMap.clear()
            positions.forEach { position -> positionMap[position.name] = position.id }
            selectedPositionId=positions.find { it.id == args.vacancy.positionId }?.id
            val selectedPositionName = positions.find { it.id == args.vacancy.positionId }?.name
            binding.autocompletePositiontextview.setText(selectedPositionName ?: "", false)

            setupAutoCompleteTextView()
        }

        viewModel.structures.observe(viewLifecycleOwner) { structures ->
            structureMap.clear()
            structures.forEach { structure -> structureMap[structure.name] = structure.id }
            selectedStructureId=structures.find { it.id == args.vacancy.structureId }?.id
            val selectedStructureName = structures.find { it.id == args.vacancy.structureId }?.name
            binding.autocompleteStructuretextview.setText(selectedStructureName ?: "", false)

            setupAutoCompleteTextView()
        }

        viewModel.vacancy.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.editText.setText(it.title ?: "")
                binding.editText2.setText(it.description ?: "")
                binding.editText3.setText(formatDateTime(it.startDate ?: ""))
            }
        }

        viewModel.afterUpdateResult.observe(viewLifecycleOwner) { afterUpdateResult ->
            lifecycleScope.launch {
                if (afterUpdateResult) {
                    customResultDialog("Successful!", "Please wait a moment, we are preparing for you...", R.color.DeepPurple)
                    delay(2500)
                    findNavController().navigate(VacancyUpdateFragmentDirections.actionVacancyUpdateFragmentToVacancyReadFragment())
                } else {
                    delay(500)
                    // Handle unsuccessful update
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

    private fun formatDateTime(dateTimeString: String): String {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")

        return try {
            val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }

    private fun customResultDialog(title: String, text: String, colorId: Int) {
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
}
