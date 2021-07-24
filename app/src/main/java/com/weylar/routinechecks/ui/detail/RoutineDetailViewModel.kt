package com.weylar.routinechecks.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.model.Status
import com.weylar.routinechecks.model.dummyRoutine
import com.weylar.routinechecks.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel
@Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _routine: MutableStateFlow<Routine> = MutableStateFlow(dummyRoutine)
    val routine: StateFlow<Routine> = _routine



     fun computePerformance(done: Int, missed: Int): Int {
         println("Done ${done}")
         println("Missed ${missed}")
         println("total ${done + missed}")
         //println("final ${(done/(done + missed)) * 100}")
        return 70
    }

    fun getRoutine(id: Long): Flow<Routine> {
        val result = MutableStateFlow(dummyRoutine)
        viewModelScope.launch(Main) {
            _routine.value = routineRepository.getRoutine(id)
        }
        return result
    }




}