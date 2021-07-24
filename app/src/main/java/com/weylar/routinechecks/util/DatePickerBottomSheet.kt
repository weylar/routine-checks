package com.weylar.routinechecks.util


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weylar.routinechecks.R
import com.weylar.routinechecks.databinding.DatePickerBottomSheetBinding
import com.weylar.routinechecks.model.DateTimeDTO
import com.weylar.routinechecks.model.Frequency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


const val TAG_CALENDAR = "CALENDAR"

class DatePickerBottomSheet : BottomSheetDialogFragment() {


    private var _binding: DatePickerBottomSheetBinding? = null
    private lateinit var data: DateTimeDTO
    private var year: Int? = null
    private var month: Int? = null
    private var dayOfMonth: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DatePickerBottomSheetBinding.inflate(inflater)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adjustViewHeight()

    }


    fun showSheet(fragment: Fragment, callback: (DateTimeDTO) -> Unit) {
        show(fragment.parentFragmentManager, TAG_CALENDAR)
        lifecycleScope.launch {
            delay(100)
            updateView(callback)
        }

    }


    private fun updateView(callback: (DateTimeDTO) -> Unit) {
        _binding?.apply {
            calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                this@DatePickerBottomSheet.year = year
                this@DatePickerBottomSheet.month = month
                this@DatePickerBottomSheet.dayOfMonth = dayOfMonth
            }
            done.setOnClickListener {
                callback.invoke(captureDateTime())
                this@DatePickerBottomSheet.dismiss()
            }
            setTimeTextView.setOnClickListener {
                showTimePicker()
            }
            removeTimeButton.setOnClickListener {
                closeTimePicker(it)
            }
            setFrequencyTextView.setOnClickListener {
                showFrequencyPicker()
            }
            removeFrequencyButton.setOnClickListener {
                closeFrequencyPicker()
            }
        }
    }

    private fun captureDateTime() = DateTimeDTO(
        date = DateHelper.buildDate(
            year ?: Calendar.getInstance().get(Calendar.YEAR),
            month ?: Calendar.getInstance().get(Calendar.MONTH),
            dayOfMonth ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            _binding?.timePicker?.currentHour!!,
            _binding?.timePicker?.currentMinute!!,
        ),
        frequency = when (_binding?.radioGroup?.checkedRadioButtonId) {
            R.id.hourly_radio -> Frequency.HOURLY
            R.id.daily_radio -> Frequency.DAILY
            R.id.weekly_radio -> Frequency.WEEKLY
            R.id.monthly_radio -> Frequency.MONTHLY
            R.id.yearly_radio -> Frequency.YEARLY
            else -> Frequency.HOURLY
        }
    )


    private fun DatePickerBottomSheetBinding.closeFrequencyPicker() {
        radioGroup.visibility = View.GONE
        removeFrequencyButton.visibility = View.GONE
        calendar.visibility = View.VISIBLE
        calendarLine.visibility = View.VISIBLE
        setTimeTextView.visibility = View.VISIBLE
        setTimeLine.visibility = View.VISIBLE
        done.visibility = View.VISIBLE
    }

    private fun DatePickerBottomSheetBinding.showFrequencyPicker() {
        removeFrequencyButton.visibility = View.VISIBLE
        radioGroup.visibility = View.VISIBLE
        timePicker.visibility = View.GONE
        calendar.visibility = View.GONE
        calendarLine.visibility = View.GONE
        setTimeTextView.visibility = View.GONE
        setTimeLine.visibility = View.GONE
        done.visibility = View.INVISIBLE
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            closeFrequencyPicker()
            setFrequencyTextView.text = when(checkedId){
                R.id.hourly_radio -> "Hourly"
                R.id.daily_radio -> "Daily"
                R.id.weekly_radio -> "Weekly"
                R.id.monthly_radio -> "Monthly"
                R.id.yearly_radio -> "Yearly"
                else -> "Hourly"
            }
        }
    }

    private fun DatePickerBottomSheetBinding.closeTimePicker(it: View) {
        timePicker.visibility = View.GONE
        it.visibility = View.GONE
        calendar.visibility = View.VISIBLE
        calendarLine.visibility = View.VISIBLE
    }

    private fun DatePickerBottomSheetBinding.showTimePicker() {
        timePicker.visibility = View.VISIBLE
        removeTimeButton.visibility = View.VISIBLE
        calendar.visibility = View.GONE
        calendarLine.visibility = View.GONE
        radioGroup.visibility = View.GONE
        removeFrequencyButton.visibility = View.GONE
    }


    private fun adjustViewHeight() {
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface DatePickerCallback {
        fun onDismiss(dateTimeDTO: DateTimeDTO)
    }

}
