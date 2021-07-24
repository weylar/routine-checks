package com.weylar.routinechecks.repository

import com.weylar.routinechecks.local.dataSource.LocalDataSource
import com.weylar.routinechecks.local.mapper.RoutineMapper
import com.weylar.routinechecks.model.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val mapper: RoutineMapper
) : RoutineRepository {

    override suspend fun saveRoutine(routine: Routine) {
        withContext(IO) {
            localDataSource.saveRoutine(mapper.mapToRoutineEntity(routine))
        }

    }

    override suspend fun getRoutine(id: Long): Routine {
        return withContext(IO) {
            mapper.mapFromRoutineEntity(localDataSource.getRoutine(id))
        }
    }

    override suspend fun getAllRoutines(): Flow<List<Routine>> {
        val result = MutableStateFlow<List<Routine>>(listOf())
        CoroutineScope(IO).launch {
            localDataSource.getAllRoutines().collect {
                result.value = mapper.mapFromListRoutineEntity(it)
            }
        }
        return result
    }
}