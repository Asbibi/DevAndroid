package com.andrealouis.devmobile.network

import com.andrealouis.devmobile.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

        // Ces deux variables encapsulent la même donnée:
        // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    //private val _taskList = MutableLiveData<List<Task>>()
        // [taskList] est publique mais non-modifiable:
        // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    //public val taskList: LiveData<List<Task>> = _taskList



    suspend fun refresh(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
        /*
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
        */
    }

    suspend fun deleteTask(task: Task) : String?{
        val deletedTask = tasksWebService.deleteTask(task.id)
        return if (deletedTask.isSuccessful) deletedTask.body() else null
        /*val deletedTask = tasksWebService.deleteTask(task.id)
        if (deletedTask.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList.removeAt(position)
            _taskList.value = editableList
        }*/
    }

    suspend fun createTask(task: Task) : Task? {
        val createdTask = tasksWebService.createTask(task)
        return if (createdTask.isSuccessful) createdTask.body() else null
        /*val createdTask = tasksWebService.createTask(task)
        if (createdTask.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            /*if (createdTask.body() == null)
                editableList.add(Task("1","NULL","c'est trop nul"))
            else
                */
            editableList.add(createdTask.body()!!)
            _taskList.value = editableList
        }*/
    }

    suspend fun updateTask(task: Task) : Task? {
        val editedTask = tasksWebService.updateTask(task)
        return if (editedTask.isSuccessful) editedTask.body() else null
        /*val editedTask = tasksWebService.updateTask(task)
        if (editedTask.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList[position] = editedTask.body()!!
            _taskList.value = editableList
        }*/
    }


}