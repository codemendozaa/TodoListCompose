package com.example.todolistcompose.addTasks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * from TaskEntity")
    fun getTask(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(item:TaskEntity)

    @Update
    suspend fun updateTask(item: TaskEntity)

    @Delete
    suspend fun deleteTask(item: TaskEntity)

}