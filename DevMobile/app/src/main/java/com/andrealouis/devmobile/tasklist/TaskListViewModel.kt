package com.andrealouis.devmobile.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.andrealouis.devmobile.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun loadTasks() {
        val fetchedTasks = repository.refresh()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
    }
    suspend fun deleteTask(task: Task) {
        val idDeletedTask = repository.deleteTask(task)
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList.removeAt(position)
            _taskList.value = editableList
        }
    }
    fun addTask(task: Task) {}
    fun editTask(task: Task) {}

}