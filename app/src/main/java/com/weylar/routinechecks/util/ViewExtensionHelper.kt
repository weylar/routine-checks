package com.weylar.routinechecks.util

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.weylar.routinechecks.R
import com.weylar.routinechecks.model.DateTimeDTO
import com.weylar.routinechecks.model.Frequency
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.model.Status
import com.weylar.routinechecks.util.DateHelper.convertMilliToDateString
import java.util.*


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Context.showDialog(fragment: Fragment, routine: Routine? = null, save: (Routine) -> Unit): Dialog {
    val dialog = Dialog(this)
    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    dialog.setContentView(R.layout.add_routine_dialog_layout)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    val titleTv = dialog.findViewById<TextView>(R.id.title)
    val descriptionTv = dialog.findViewById<TextView>(R.id.description)
    val saveTv = dialog.findViewById<TextView>(R.id.save_text_view)
    val dateTimeRel = dialog.findViewById<RelativeLayout>(R.id.date_time_view)
    val dateTime = dialog.findViewById<TextView>(R.id.date_time)
    val descriptionIcon = dialog.findViewById<ImageView>(R.id.description_icon)
    val calendarIcon = dialog.findViewById<ImageView>(R.id.calendar_icon)
    var dateTimeDTO: DateTimeDTO? = null

    titleTv.text = routine?.title?:""
    descriptionTv.text = routine?.description?:""
    dateTime.text = routine?.date?.convertMilliToDateString()?:""


    titleTv.addTextChangedListener {
        if (!it.isNullOrEmpty()) {
            saveTv.alpha = 1f
            saveTv.setOnClickListener {
                dialog.dismiss()
                save.invoke(
                    Routine(
                        title = titleTv.text.trim().toString(),
                        description = descriptionTv.text.trim().toString(),
                        date = dateTimeDTO?.date ?: 0L,
                        nextUpTime = dateTimeDTO?.date ?: 0L,
                        frequency = Frequency.DAILY,
                        lastStatus = Status.UNKNOWN
                    )
                )
            }

        } else {
            saveTv.alpha = 0.5f
            saveTv.setOnClickListener(null)
        }
    }

    descriptionIcon.setOnClickListener {
        if (!descriptionTv.isVisible) descriptionTv.visibility = View.VISIBLE
    }

    calendarIcon.setOnClickListener {
        fragment.openSheetAddress {
            dateTimeDTO = it
            dateTimeRel.visibility = View.VISIBLE
            dateTimeRel.findViewById<TextView>(R.id.date_time).text = it.date.convertMilliToDateString()
            dateTimeRel.findViewById<ImageView>(R.id.cancel_button).setOnClickListener {
                dateTimeRel.visibility = View.GONE
            }
        }
    }

    return dialog
}

private fun Fragment.openSheetAddress(callback: (DateTimeDTO) -> Unit) {
    val frag = parentFragmentManager.findFragmentByTag(TAG_CALENDAR)
    if (frag == null) {
        DatePickerBottomSheet().apply {
            setTargetFragment(this@openSheetAddress, 32)
            showSheet(this@openSheetAddress, callback)
        }
    }
}

