package com.andrealouis.devmobile.paging

import android.util.Log
import androidx.paging.PagingSource
import com.andrealouis.devmobile.network.Api
import com.andrealouis.devmobile.task.Task

class TaskListPagingSource() : PagingSource<Int, Task>() {

    val tasksWebService = Api.INSTANCE.tasksWebService

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Task> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = tasksWebService.getTasks(nextPageNumber)
            return LoadResult.Page(
                    data = response.body()!!,
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageNumber + 1//response.nextPageNumber
            )
        } catch (e: Exception) {
            Log.d("PAGING :","Error loading page...")
            return LoadResult.Error(e)
        } catch (e: NullPointerException) {
            Log.d("PAGING :","Error loading page... NullPointer")
            return LoadResult.Error(e)
        }
    }

    suspend fun deleteTask(task: Task) : Boolean{
        Log.d("DELETE_REP", "on est dans le deleteTask du Repository")
        val deletedTask = tasksWebService.deleteTask(task.id)
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