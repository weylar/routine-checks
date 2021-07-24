package com.weylar.routinechecks.local.mapper

import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.model.Routine

interface RoutineMapper {

    fun mapToRoutineEntity(routine: Routine): RoutineEntity
    fun mapFromRoutineEntity(routineEntity: RoutineEntity): Routine
    fun mapFromListRoutineEntity(routineEntity: List<RoutineEntity>): List<Routine>
}