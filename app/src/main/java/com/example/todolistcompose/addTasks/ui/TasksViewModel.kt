package com.example.todolistcompose.addTasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistcompose.addTasks.ui.model.TaskModel
import javax.inject.Inject

class TasksViewModel @Inject constructor() :ViewModel() {


    private val _showDialog = MutableLiveData<Boolean>()
    var showDialog:LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks:List<TaskModel> = _tasks

    fun onDialogClose() {
       _showDialog.value = false
    }

    fun onTaskCreate(task: String) {
        _showDialog.value = false
       _tasks.add(TaskModel(task = task))
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {

    }
}


