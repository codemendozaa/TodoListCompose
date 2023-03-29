package com.example.todolistcompose.addTasks.domain

import com.example.todolistcompose.addTasks.data.TaskRepository
import com.example.todolistcompose.addTasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private  val taskRepository: TaskRepository) {
    suspend operator fun  invoke(taskModel: TaskModel){
        taskRepository.add(taskModel)
    }
}