package com.weylar.routinechecks.local.dataSource

import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.local.room.RoutineDatabase
import com.weylar.routinechecks.local.room.dao.RoutineDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSourceImpl @Inject constructor(
    private val database: RoutineDatabase
) : LocalDataSource {

    override fun saveRoutine(routineEntity: RoutineEntity) {
        database.routineDao().insert(routineEntity)
    }

    override fun getAllRoutines(): Flow<List<RoutineEntity>> {
        return database.routineDao().getAll()
    }

    override fun getRoutine(id: Long): RoutineEntity {
        return database.routineDao().getRoutine(id)
    }
}