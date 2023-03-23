package com.example.todolistcompose.addTasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todolistcompose.addTasks.ui.TasksViewModel

@Composable

fun TasksScreen(tasksViewModel: TasksViewModel) {

    val showDialog:Boolean by tasksViewModel.showDialog.observeAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize()){
        AddTasksDialog(show = showDialog, onDismiss = {tasksViewModel.onDialogClose()}, onTaskAdded = {tasksViewModel.onTaskCreate(it)} )
        FabDialog(Modifier.align(Alignment.BottomEnd),tasksViewModel)
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
                 },Modifier.fillMaxWidth()) {
                    Text(text = "Enviar Tarea")
                }
            }
            
        }
    }
}
