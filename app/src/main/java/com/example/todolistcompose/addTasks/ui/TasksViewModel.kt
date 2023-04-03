package com.example.todolistcompose.addTasks.ui


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistcompose.addTasks.domain.AddTaskUseCase
import com.example.todolistcompose.addTasks.domain.GetTaskUseCase
import com.example.todolistcompose.addTasks.ui.TaskUIState.Success
import com.example.todolistcompose.addTasks.ui.model.TaskModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    getTaskUseCase: GetTaskUseCase
) :ViewModel() {

    val uiState: StateFlow<TaskUIState> = getTaskUseCase().map (::Success )
        .catch { TaskUIState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),TaskUIState.Loading)

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

        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)
        _tasks [index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)
    }
}


