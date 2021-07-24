package com.weylar.routinechecks.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.model.Status
import com.weylar.routinechecks.model.dummyRoutine
import com.weylar.routinechecks.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _getRoutineCallback: MutableStateFlow<List<Routine>> = MutableStateFlow(listOf())
    val getRoutineCallback: StateFlow<List<Routine>> = _getRoutineCallback

    init {
        fetchAllRoutine()
    }

    private fun fetchAllRoutine() {
        viewModelScope.launch(Main) {
            routineRepository.getAllRoutines()
                .collect {
                    _getRoutineCallback.value = it.asReversed()
                }
        }
    }

    fun saveRoutine(routine: Routine): Flow<Unit> {
        val result = MutableStateFlow(Unit)
        viewModelScope.launch(Main) {
            delay(500)
            routineRepository.saveRoutine(routine)
            result.value = Unit
        }
        return result
    }

    fun getRoutine(routine: Routine): Flow<Routine> {
        val result = MutableStateFlow(dummyRoutine)
        viewModelScope.launch(Main) {
            result.value = routineRepository.getRoutine(routine.id!!)
        }
        return result
    }

    fun markAsDone(routine: Routine) {
        viewModelScope.launch(Main) {
            val result = routineRepository.getRoutine(routine.id!!)
            val updatedRoutine =
                result.copy(lastStatus = Status.DONE, doneCount = ++result.doneCount)
            routineRepository.saveRoutine(updatedRoutine)
        }
    }


}