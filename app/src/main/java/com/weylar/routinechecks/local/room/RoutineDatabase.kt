package com.weylar.routinechecks.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.local.room.dao.RoutineDao


@Database(
    entities = [RoutineEntity::class],
    version = 8,
    exportSchema = true,
)

@TypeConverters(Converters::class)
abstract class RoutineDatabase : RoomDatabase() {


    abstract fun routineDao(): RoutineDao

    companion object {
        const val DATABASE_NAME: String = "routine_db"

        fun closeDatabase() {
            instance?.close()
            instance = null
        }

        @Volatile
        private var instance: RoutineDatabase? = null

        fun getInstance(context: Context): RoutineDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): RoutineDatabase {
            return Room.databaseBuilder(
                context,
                RoutineDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}