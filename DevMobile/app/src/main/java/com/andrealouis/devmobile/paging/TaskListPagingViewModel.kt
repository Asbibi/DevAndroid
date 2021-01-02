package com.andrealouis.devmobile.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class TaskListPagingViewModel : ViewModel()  {

    /*val taskList = Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(pageSize = 20)
    ) {TaskListPagingSource()}.liveData.cachedIn(viewModelScope) //flow.cachedIn(viewModelScope)*/
    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 20)
    ) {TaskListPagingSource()}.flow.cachedIn(viewModelScope)

    //private val repository = TasksRepository()
    //private val _taskList = MutableLiveData<List<Task>>()
    //public val taskList: LiveData<List<Task>> = _taskList

/*
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
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            val createdTask = repository.createTask(task)
            if (createdTask != null) {
                val editableList = _taskList.value.orEmpty().toMutableList()
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
*/
}