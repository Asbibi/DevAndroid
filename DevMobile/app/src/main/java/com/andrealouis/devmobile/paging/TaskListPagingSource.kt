package com.andrealouis.devmobile.paging

import android.util.Log
import androidx.paging.PagingSource
import com.andrealouis.devmobile.network.Api
import com.andrealouis.devmobile.task.Task
import retrofit2.HttpException
import java.io.IOException

class TaskListPagingSource() : PagingSource<Int, Task>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Task> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = Api.INSTANCE.tasksWebService.getTasks(nextPageNumber)
            return LoadResult.Page(
                    data = response.body()!!,
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageNumber + 1//response.nextPageNumber
            )
        } catch (e: IOException) {
            Log.d("PAGING :","Error loading page...")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d("PAGING :","Error loading page...")
            return LoadResult.Error(e)
        }
    }


}