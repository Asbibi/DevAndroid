package com.andrealouis.devmobile.paging

import androidx.recyclerview.widget.DiffUtil
import com.andrealouis.devmobile.task.Task

object TaskListPagingComparator  : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}