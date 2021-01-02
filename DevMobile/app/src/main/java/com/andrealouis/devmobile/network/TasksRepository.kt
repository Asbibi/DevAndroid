package com.andrealouis.devmobile.network

import android.util.Log
import com.andrealouis.devmobile.task.Task

class TasksRepository {
    private val tasksWebService = Api.INSTANCE.tasksWebService

        // Ces deux variables encapsulent la même donnée:
        // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    //private val _taskList = MutableLiveData<List<Task>>()
        // [taskList] est publique mais non-modifiable:
        // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    //public val taskList: LiveData<List<Task>> = _taskList



    suspend fun refresh(): List<Task>? {
        val response = tasksWebService.getTasks(0)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteTask(task: Task) : Boolean{
        Log.d("DELETE_REP", "on est dans le deleteTask du Repository")
        val deletedTask = tasksWebService.deleteTask(task.id)
        /*if (deletedTask == null)
            Log.d("DELETE_REP", "Reponse nulle")
        if (deletedTask.isSuccessful) {
            Log.d("DELETE_REP", "Reponse successful")
            if (deletedTask.body() == null)
                Log.d("DELETE_REP", "Body de la réponse est null")
            else
                Log.d("DELETE_REP", deletedTask.body().toString())
        }
        else
            Log.d("DELETE_REP", "Reponse echec")
        return if (deletedTask.isSuccessful) deletedTask.body() else null*/
        return deletedTask.isSuccessful
    }

    suspend fun createTask(task: Task) : Task? {
        val createdTask = tasksWebService.createTask(task)
        return if (createdTask.isSuccessful) createdTask.body() else null
    }

    suspend fun updateTask(task: Task) : Task? {
        val editedTask = tasksWebService.updateTask(task)
        return if (editedTask.isSuccessful) editedTask.body() else null
    }
}