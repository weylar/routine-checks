package com.weylar.routinechecks.ui.detail

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.weylar.routinechecks.R
import com.weylar.routinechecks.databinding.RoutineDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val ID = "ID"
@AndroidEntryPoint
class RoutineDetailFragment : Fragment(R.layout.routine_detail_fragment) {


    private val viewModel by viewModels<RoutineDetailViewModel>()
    private var _binding: RoutineDetailFragmentBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RoutineDetailFragmentBinding.bind(view)
        updateView(requireArguments().getLong(ID))

    }

    private fun updateView(id: Long) {
        viewModel.getRoutine(id)
        viewModel.routine.onEach {
            if(it.id != 0L) {
                _binding?.titleTextView?.text = it.title
                _binding?.descriptionTextView?.text = it.description
                _binding?.nextUpTextView?.text =
                    "Your next routine check is ${DateUtils.getRelativeTimeSpanString(it.nextUpTime)}"
                val result = viewModel.computePerformance(it.doneCount, it.missedCount)
                if (result >= 70) {
                    _binding?.sadIcon?.setImageResource(R.drawable.happy_icon)
                    _binding?.rateDescription?.text =
                        "Good job! You have over 70% check for this routine"
                } else {
                    _binding?.sadIcon?.setImageResource(R.drawable.sad_icon)
                    _binding?.rateDescription?.visibility = View.GONE
                }
            }


        }.launchIn(lifecycleScope)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}