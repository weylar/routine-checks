package com.weylar.routinechecks.repository

import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {

    suspend fun saveRoutine(routine: Routine)
    suspend fun getRoutine(id: Long): Routine
    suspend fun getAllRoutines(): Flow<List<Routine>>
}