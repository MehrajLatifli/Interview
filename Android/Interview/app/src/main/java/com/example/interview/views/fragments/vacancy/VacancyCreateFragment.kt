package com.example.interview.views.fragments.vacancy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.example.interview.models.responses.post.vacancy.VacancyRequest
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
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class VacancyCreateFragment : BaseFragment<FragmentVacancyCreateBinding>(FragmentVacancyCreateBinding::inflate) {

    private val positionMap = mutableMapOf<String, String>()
    private val structureMap = mutableMapOf<String, String>()
    private var selectedPositionId: String = ""
    private var selectedStructureId: String = ""

    private val viewModel by viewModels<VacancyViewModel>()

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dropdownBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.autocompletetextview_radiuscolor)
        binding.autocompletePositiontextview.setDropDownBackgroundDrawable(dropdownBackground)
        binding.autocompleteStructuretextview.setDropDownBackgroundDrawable(dropdownBackground)

        observeData()

        lifecycleScope.launch {
            fetchAndSetupData()
        }

        binding.buttonDateTime.setOnClickListener {
            showDatePicker()
        }

        binding.autocompletePositiontextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompletePositiontextview.adapter.getItem(position) as? String
            selectedPositionId = positionMap[selectedItemName] ?: ""
        }

        binding.autocompleteStructuretextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName = binding.autocompleteStructuretextview.adapter.getItem(position) as? String
            selectedStructureId = structureMap[selectedItemName] ?: ""
        }

        binding.buttonCreate.setOnClickListener {
            handleCreateVacancy()
        }
        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)
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

                val hintColor = ContextCompat.getColor(requireContext(), R.color.White)

                val colorStateList =ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Black))

                binding.editText.setHintTextColor(hintColor)
                binding.editText2.setHintTextColor(hintColor)
                binding.editText3.setHintTextColor(hintColor)
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
            lifecycleScope.launch(Dispatchers.Main) {
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
                Log.e("VacancyCreate", errorMessage)
                customResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
            }
        }

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            positionMap.clear()
            positions.forEach { position ->
                positionMap[position.name] = position.id.toString()
            }
            setupAutoCompleteTextView()
        }

        viewModel.structures.observe(viewLifecycleOwner) { structures ->
            structureMap.clear()
            structures.forEach { structure ->
                structureMap[structure.name] = structure.id.toString()
            }
            setupAutoCompleteTextView()
        }

        viewModel.completeResult.observe(viewLifecycleOwner) { completeResult ->
            lifecycleScope.launch {
                if (completeResult) {
                    customResultDialog(
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

    private suspend fun fetchAndSetupData() {
        val positions = viewModel.getAllPositions()
        positionMap.clear()
        positions.forEach { position ->
            positionMap[position.name] = position.id.toString()
        }
        setupAutoCompleteTextView()

        val structures = viewModel.getAllStructures()
        structureMap.clear()
        structures.forEach { structure ->
            structureMap[structure.name] = structure.id.toString()
        }
        setupAutoCompleteTextView()
    }

    private fun handleCreateVacancy() {
        val startDateTime = formatDateTimeforCalendar(calendar)
        val endDateCalendar = addMonthSafely(calendar)
        val endDateTime = formatDateTimeforCalendar(endDateCalendar)

        if (binding.editText.text.isNullOrBlank()) {
            customResultDialog("Unsuccessful!", "Write title", R.color.MellowMelon)
            return
        }

        if (binding.editText2.text.isNullOrBlank()) {
            customResultDialog("Unsuccessful!", "Write description", R.color.MellowMelon)
            return
        }

        if (binding.editText3.text.isNullOrBlank()) {
            customResultDialog("Unsuccessful!", "Write date", R.color.MellowMelon)
            return
        }

        if (startDateTime.isBlank()) {
            customResultDialog("Unsuccessful!", "Write startDate", R.color.MellowMelon)
            return
        }

        if (endDateTime.isBlank()) {
            customResultDialog("Unsuccessful!", "Write endDate", R.color.MellowMelon)
            return
        }

        if (selectedPositionId.isBlank()) {
            customResultDialog("Unsuccessful!", "Select position", R.color.MellowMelon)
            return
        }

        if (selectedStructureId.isBlank()) {
            customResultDialog("Unsuccessful!", "Select structure", R.color.MellowMelon)
            return
        }

        val vacancyRequest = VacancyRequest(
            title = binding.editText.text.toString(),
            description = binding.editText2.text.toString(),
            startDate = startDateTime,
            endDate = endDateTime,
            positionId = selectedPositionId.toIntOrNull() ?: 0,
            structureId = selectedStructureId.toIntOrNull() ?: 0
        )

        viewModel.addVacancy(vacancyRequest)
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
                calendar.set(year, month, dayOfMonth)
                showTimePicker()
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
                binding.editText3.setText("${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}")
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}
