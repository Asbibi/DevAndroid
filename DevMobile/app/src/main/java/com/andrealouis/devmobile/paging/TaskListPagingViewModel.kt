package com.andrealouis.devmobile.paging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andrealouis.devmobile.task.Task
import kotlinx.coroutines.launch

class TaskListPagingViewModel : ViewModel()  {

    var repository = TaskListPagingSource()
    val flow = Pager(PagingConfig(pageSize = 20)) { TaskListPagingSource().also {
        repository = it
    } }.flow.cachedIn(viewModelScope)

    fun deleteTask(task: Task) {
        Log.d("DELETE", "method deleteTask du viewModel appelée")
        viewModelScope.launch {
            val deletedTaskSuccessful = repository.deleteTask(task)
            if (deletedTaskSuccessful){
                repository.invalidate()
            }
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            val createdTask = repository.createTask(task)
            if (createdTask != null) {
                repository.invalidate()
            }
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            val editedTask = repository.updateTask(task)
            if (editedTask != null) {
                repository.invalidate()
            }
        }
    }
}