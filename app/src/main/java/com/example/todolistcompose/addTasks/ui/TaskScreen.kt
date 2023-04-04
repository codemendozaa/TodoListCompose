package com.example.todolistcompose.addTasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.todolistcompose.addTasks.ui.model.TaskModel

@Composable

fun TasksScreen(tasksViewModel: TasksViewModel) {

    val showDialog:Boolean by tasksViewModel.showDialog.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUIState>(
        initialValue = TaskUIState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel
    ){
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED){
            tasksViewModel.uiState.collect{value = it}
        }
    }

    when(uiState){

        is TaskUIState.Error -> {}
        TaskUIState.Loading -> { CircularProgressIndicator()}
        is TaskUIState.Success -> {
            Box(modifier = Modifier.fillMaxSize()){
                AddTasksDialog(show = showDialog, onDismiss = {tasksViewModel.onDialogClose()}, onTaskAdded = {tasksViewModel.onTaskCreate(it)} )
                FabDialog(Modifier.align(Alignment.BottomEnd),tasksViewModel)
                TasksList((uiState as TaskUIState.Success).tasks,tasksViewModel)
            }
        }

    }


}

@Composable
fun TasksList(tasks: List<TaskModel>, tasksViewModel:TasksViewModel) {

    LazyColumn(){
        items(tasks, key = {it.id}){ task->
            ItemTask(taskModel = task, tasksViewModel = tasksViewModel)
        }
    }
}


@Composable
fun ItemTask(taskModel:TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    tasksViewModel.onItemRemove(taskModel)
                })
            },
             elevation = 8.dp
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskModel.task,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp))
            Checkbox(checked = taskModel.selected, onCheckedChange = {tasksViewModel.onCheckBoxSelected(taskModel) })

        }
    }

}

@Composable
fun FabDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(onClick = {
        //mostrar un dialogo
        tasksViewModel.onShowDialogClick()
    }, modifier = modifier
        .padding(16.dp)) {
        Icon( Icons.Filled.Add, contentDescription = "add")
    }
}


@Composable
fun AddTasksDialog(show:Boolean,onDismiss:()->Unit, onTaskAdded:(String)->Unit){
    var myTask by remember { mutableStateOf("") }
    if (show){
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)) {
                Text(
                    text = "Añade tu tarea",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value =  myTask , onValueChange = {myTask = it}, singleLine = true, maxLines = 1 )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = { //enviar tarea
                                 onTaskAdded(myTask)
                                 myTask = ""
                 },Modifier.fillMaxWidth()) {
                    Text(text = "Enviar Tarea")
                }
            }
            
        }
    }
}
