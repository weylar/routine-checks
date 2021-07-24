package com.weylar.routinechecks.ui.nextup

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.weylar.routinechecks.R
import com.weylar.routinechecks.databinding.NextUpFragmentBinding
import com.weylar.routinechecks.databinding.RoutineDetailFragmentBinding
import com.weylar.routinechecks.local.mapper.RoutineMapper
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.ui.detail.ID
import com.weylar.routinechecks.ui.home.RoutineAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class NextUpFragment : Fragment(R.layout.next_up_fragment) {


    private val viewModel by viewModels<NextUpViewModel>()
    private var _binding: NextUpFragmentBinding? = null
    private lateinit var adapter: RoutineAdapter

    @Inject
    lateinit var mapperImpl: RoutineMapper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NextUpFragmentBinding.bind(view)
        updateView()


    }


    private fun setUpAdapter() = RoutineAdapter(object : RoutineAdapter.RoutineClickListener {
        override fun onRoutineClick(routine: Routine) {

        }

        override fun onRoutineDoneClick(routine: Routine) {

        }

        override fun onEditClick(routine: Routine) {

        }
    })

    private fun updateView() {
        _binding?.apply {
            adapter = setUpAdapter()
            routinesRecyclerView.adapter = adapter

        }

        fetchAllRoutines()
    }

    private fun fetchAllRoutines() {
        viewModel.getRoutineCallback.onEach {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
                _binding?.emptyView?.visibility = View.GONE
            } else {
                _binding?.emptyView?.visibility = View.VISIBLE
            }
        }.launchIn(lifecycleScope)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}