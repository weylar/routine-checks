package com.weylar.routinechecks.ui.home


import android.graphics.Color
import android.graphics.Paint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weylar.routinechecks.databinding.RoutineListItemBinding
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.model.Status


class RoutineAdapter(
    private val clickListener: RoutineClickListener
) : ListAdapter<Routine, RoutineAdapter.ViewHolder>(ROUTINE_COMPARATOR) {

    class ViewHolder(private val binding: RoutineListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Routine, click: RoutineClickListener) {
            binding.run {
                root.setOnClickListener { click.onRoutineClick(data) }
                editIcon.setOnClickListener {
                    click.onEditClick(data)
                }
                checkRadioButton.setOnClickListener {
                    click.onRoutineDoneClick(data)
                    strikeOutRoutine()
                }
                routineTitle.text = data.title
                routineDescription.text = data.description
                routineTime.text = DateUtils.getRelativeTimeSpanString(data.nextUpTime)

                when (data.lastStatus) {
                    Status.DONE -> {
                        status.text = "Done"
                        status.setTextColor(Color.GREEN)
                        status.visibility = View.VISIBLE
                    }
                    Status.MISSED -> {
                        status.text = "Missed"
                        status.setTextColor(Color.RED)
                        status.visibility = View.VISIBLE
                    }
                    else -> {
                        status.visibility = View.GONE
                    }
                }
                if (!data.description.isNullOrEmpty()) {
                    routineDescription.visibility = View.VISIBLE
                } else {
                    routineDescription.visibility = View.GONE
                }
                if (data.lastStatus == Status.DONE) {
                    strikeOutRoutine()
                }
            }
        }

        private fun RoutineListItemBinding.strikeOutRoutine() {
            routineTitle.paintFlags = routineTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            routineDescription.paintFlags =
                routineDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            routineTime.paintFlags = routineTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            RoutineListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            viewHolder.bind(data, clickListener)
        }

    }


    companion object {
        private val ROUTINE_COMPARATOR = object : DiffUtil.ItemCallback<Routine>() {
            override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean =
                oldItem == newItem
        }
    }

    interface RoutineClickListener {
        fun onRoutineClick(routine: Routine)
        fun onRoutineDoneClick(routine: Routine)
        fun onEditClick(routine: Routine)
    }

}