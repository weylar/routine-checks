package com.weylar.routinechecks.ui.nextup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weylar.routinechecks.model.Routine
import com.weylar.routinechecks.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NextUpViewModel
@Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _getRoutineCallback: MutableStateFlow<List<Routine>> = MutableStateFlow(listOf())
    val getRoutineCallback: StateFlow<List<Routine>> = _getRoutineCallback

    init {
        fetchUpNextRoutine()
    }

    private fun fetchUpNextRoutine() {
        viewModelScope.launch(Main) {
            routineRepository.getAllRoutines()
                .collect {
                    _getRoutineCallback.value = it.asReversed()
                        .filter { routine -> (Calendar.getInstance().timeInMillis - routine.nextUpTime) <= 12 * 60 * 60 * 1000L }
                }
        }
    }


}