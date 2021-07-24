package com.weylar.routinechecks.local.dataSource

import com.weylar.routinechecks.local.entity.RoutineEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun saveRoutine(routineEntity: RoutineEntity)
    fun getAllRoutines(): Flow<List<RoutineEntity>>
    fun getRoutine(id: Long): RoutineEntity
}