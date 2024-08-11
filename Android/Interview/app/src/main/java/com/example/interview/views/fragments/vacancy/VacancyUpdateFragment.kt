package com.example.interview.views.fragments.vacancy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentVacancyUpdateBinding
import com.example.interview.models.responses.get.vacancy.VacancyResponse
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

        binding.buttonDateTime.setOnClickListener {
            showDatePicker()
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

            if (!startDateTime.contains(args.vacancy.startDate) && !endDateTime.contains(args.vacancy.endDate)) {

                val inputFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS", Locale.getDefault())
                val outputFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())


                val dateString = binding.editText3.text.toString()
                val dateTime = LocalDateTime.parse(dateString, inputFormatter)
                val newDateTime = dateTime.plusMonths(1)

                newDateTime.format(outputFormatter)

                val vacancy = VacancyResponse(
                    id = args.vacancy.id,
                    title = binding.editText.text.toString(),
                    description = binding.editText2.text.toString(),
                    startDate = dateTime.format(outputFormatter),
                    endDate = newDateTime.format(outputFormatter),
                    positionId = selectedPositionId ?: 0,
                    structureId = selectedStructureId ?: 0
                )

                viewModel.updateVacancy(vacancy)


            } else {
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

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    override fun onDestroyView() {
        viewModel?.vacancies?.removeObservers(viewLifecycleOwner)
        viewModel?.positions?.removeObservers(viewLifecycleOwner)
        viewModel?.structures?.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        lifecycleScope.launch {

            binding.let { bindingitem ->


                val textInputEditText: AutoCompleteTextView = bindingitem.autocompletePositiontextview
                textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, savedPrimaryFontSize)

                val textInputEditText2: AutoCompleteTextView = bindingitem.autocompleteStructuretextview
                textInputEditText2.setTextSize(TypedValue.COMPLEX_UNIT_SP, savedPrimaryFontSize)


                val hintTextView = bindingitem.textInputLayout1.editText as? AutoCompleteTextView
                hintTextView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, savedPrimaryFontSize)

                val hintTextView2 = bindingitem.textInputLayout2.editText as? AutoCompleteTextView
                hintTextView2?.setTextSize(TypedValue.COMPLEX_UNIT_SP, savedPrimaryFontSize)


                bindingitem.editText.textSize = savedPrimaryFontSize
                bindingitem.editText2.textSize = savedPrimaryFontSize
                bindingitem.editText3.textSize = savedSecondaryFontSize
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

                binding.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))

                binding.Main.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
                binding.NestedScrollView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )

                val hintColor = ContextCompat.getColor(requireContext(), R.color.White)

                val colorStateList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Purple))

                val autoCompleteTextView = binding.autocompletePositiontextview
                autoCompleteTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.ComingUpRoses
                    )
                )

                val autoCompleteTextView2 = binding.autocompleteStructuretextview
                autoCompleteTextView2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.ComingUpRoses
                    )
                )


                binding.editText.setHintTextColor(hintColor)
                binding.editText2.setHintTextColor(hintColor)
                binding.editText3.setHintTextColor(hintColor)
                binding.editText.setTextColor(hintColor)
                binding.editText2.setTextColor(hintColor)
                binding.editText3.setTextColor(hintColor)
                binding.textInputLayout1.setHintTextColor(colorStateList)
                binding.textInputLayout2.setHintTextColor(colorStateList)



            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            lifecycleScope.launch {
                delay(2000)
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        if (isLoading) {
                            bitem.includeProgressbar.progressBar.visible()
                            bitem.mainConstraintLayout.gone()
                            bitem.NestedScrollView.gone()
                        } else {
                            bitem.includeProgressbar.progressBar.gone()
                            bitem.mainConstraintLayout.visible()
                            bitem.NestedScrollView.visible()
                        }
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (!errorMessage.isNullOrBlank()) {
                    Log.e("VacancyUpdate", errorMessage)
                    customResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                }
            }
        }

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    positionMap.clear()
                    positions.forEach { position -> positionMap[position.name] = position.id }
                    selectedPositionId = positions.find { it.id == args.vacancy.positionId }?.id
                    val selectedPositionName =
                        positions.find { it.id == args.vacancy.positionId }?.name
                    bitem.autocompletePositiontextview.setText(selectedPositionName ?: "", false)
                }

                setupAutoCompleteTextView()
            }
        }

        viewModel.structures.observe(viewLifecycleOwner) { structures ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    structureMap.clear()
                    structures.forEach { structure -> structureMap[structure.name] = structure.id }
                    selectedStructureId = structures.find { it.id == args.vacancy.structureId }?.id
                    val selectedStructureName =
                        structures.find { it.id == args.vacancy.structureId }?.name
                    bitem.autocompleteStructuretextview.setText(
                        selectedStructureName ?: "",
                        false
                    )

                    setupAutoCompleteTextView()
                }
            }
        }

        viewModel.vacancy.observe(viewLifecycleOwner) { item ->
            item?.let {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        bitem.editText.setText(it.title ?: "")
                        bitem.editText2.setText(it.description ?: "")
                        bitem.editText3.setText(formatDateTime(it.startDate ?: ""))
                    }
                }
            }
        }

        viewModel.afterUpdateResult.observe(viewLifecycleOwner) { afterUpdateResult ->
            lifecycleScope.launch {
                if (isAdded) {
                    if (afterUpdateResult) {
                        customResultDialog(
                            "Successful!",
                            "Please wait a moment, we are preparing for you...",
                            R.color.DeepPurple
                        )
                        delay(2500)
                        findNavController().navigate(VacancyUpdateFragmentDirections.actionVacancyUpdateFragmentToVacancyReadFragment())
                    } else {
                        delay(500)
                    }
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
