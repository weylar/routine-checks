package com.weylar.routinechecks.local.mapper

import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.model.Routine

class RoutineMapperImpl : RoutineMapper {

    override fun mapToRoutineEntity(routine: Routine): RoutineEntity {
        return RoutineEntity(
            id = routine.id!!,
            title = routine.title,
            description = routine.description,
            date = routine.date,
            frequency = routine.frequency,
            lastStatus = routine.lastStatus,
            doneCount = routine.doneCount,
            missedCount = routine.missedCount,
            nextUpTime = routine.nextUpTime
        )
    }


    override fun mapFromRoutineEntity(routineEntity: RoutineEntity): Routine {
        return Routine(
            id = routineEntity.id,
            title = routineEntity.title,
            description = routineEntity.description,
            date = routineEntity.date,
            frequency = routineEntity.frequency,
            lastStatus = routineEntity.lastStatus,
            doneCount = routineEntity.doneCount,
            missedCount = routineEntity.missedCount,
            nextUpTime = routineEntity.nextUpTime
        )
    }

    override fun mapFromListRoutineEntity(routineEntity: List<RoutineEntity>): List<Routine> {
        return routineEntity.map { mapFromRoutineEntity(it) }
    }


}