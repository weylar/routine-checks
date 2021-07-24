package com.weylar.routinechecks.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.weylar.routinechecks.R
import com.weylar.routinechecks.databinding.HomeFragmentBinding
import com.weylar.routinechecks.local.mapper.RoutineMapper
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.navigator.AppNavDispatcher
import com.weylar.routinechecks.ui.detail.ID
import com.weylar.routinechecks.util.showDialog
import com.weylar.routinechecks.util.showToast
import com.weylar.routinechecks.worker.RoutineWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: HomeFragmentBinding? = null
    private lateinit var adapter: RoutineAdapter

    @Inject
    lateinit var mapperImpl: RoutineMapper

    @Inject
    lateinit var navDispatcher: AppNavDispatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)
        updateView()
        addRoutine()

    }

    private fun addRoutine() {
        _binding?.addFab?.setOnClickListener {
            requireContext().showDialog(this) {
                viewModel.saveRoutine(it)
                RoutineWorker.startWorker(
                    requireContext(),
                    mapperImpl.mapToRoutineEntity(it)
                )
                requireContext().showToast(getString(R.string.routine_created_success))
            }.show()
        }
    }

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

    private fun setUpAdapter() = RoutineAdapter(object : RoutineAdapter.RoutineClickListener {
        override fun onRoutineClick(routine: Routine) {
            navDispatcher.openRoutineDetail(this@HomeFragment, Bundle().apply {
                putLong(ID, routine.id)
            })
        }

        override fun onRoutineDoneClick(routine: Routine) {
            viewModel.markAsDone(routine)
        }
    })

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}