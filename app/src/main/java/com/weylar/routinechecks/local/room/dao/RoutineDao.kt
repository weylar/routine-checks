package com.weylar.routinechecks.local.room.dao

import androidx.room.*
import com.weylar.routinechecks.local.entity.RoutineEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine")
    fun getAll(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routine WHERE id = :id")
    fun getRoutine(id: Long): RoutineEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(addresses: RoutineEntity)

    @Delete
    fun delete(address: RoutineEntity)


}
