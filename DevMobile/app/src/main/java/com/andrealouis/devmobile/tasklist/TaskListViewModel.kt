package com.andrealouis.devmobile.tasklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrealouis.devmobile.network.TasksRepository
import com.andrealouis.devmobile.task.Task
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList


    fun loadTasks() {
        viewModelScope.launch {
            val fetchedTasks = repository.refresh()
            if (fetchedTasks != null)
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
                _taskList.value = fetchedTasks!!
        }
    }
    fun deleteTask(task: Task) {
        Log.d("DELETE", "method deleteTask du viewModel appelée")
        viewModelScope.launch {
            val deletedTaskSuccessful = repository.deleteTask(task)
            if (deletedTaskSuccessful){
                Log.d("DELETE", "Delete reussi !")
                val editableList = _taskList.value.orEmpty().toMutableList()
                val position = editableList.indexOfFirst { task.id == it.id }
                editableList.removeAt(position)
                _taskList.value = editableList
            }
            /*val deletedTask = repository.deleteTask(task)
            if (deletedTask != null) {
                Log.d("DELETE", "rentré dans le if de deleteTask du viewModel")
                val editableList = _taskList.value.orEmpty().toMutableList()
                val position = editableList.indexOfFirst { task.id == it.id }
                editableList.removeAt(position)
                _taskList.value = editableList
            }
            else{
                Log.d("DELETE", "la réponse est null(e)")
            }*/
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            val createdTask = repository.createTask(task)
            if (createdTask != null) {
                val editableList = _taskList.value.orEmpty().toMutableList()
                /*if (createdTask.body() == null)
                editableList.add(Task("1","NULL","c'est trop nul"))
            else
                */
                editableList.add(createdTask)
                _taskList.value = editableList
            }
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            val editedTask = repository.updateTask(task)
            if (editedTask != null) {
                val editableList = _taskList.value.orEmpty().toMutableList()
                val position = editableList.indexOfFirst { task.id == it.id }
                editableList[position] = editedTask
                _taskList.value = editableList
            }
        }
    }

}