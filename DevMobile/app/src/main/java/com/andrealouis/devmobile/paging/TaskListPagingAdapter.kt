package com.andrealouis.devmobile.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.task.Task

class TaskListPagingAdapter (diffCallback: TaskListPagingComparator) : PagingDataAdapter<Task, TaskListPagingAdapter.TaskPagingViewHolder>(diffCallback) {

    var onEditClickListener: ((Task) -> Unit)? = null
    var onDeleteClickListener: ((Task) -> Unit)? = null

    inner class TaskPagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                var textView = itemView.findViewById<TextView>(R.id.task_title)
                textView.text = task.title
                val editButton = itemView.findViewById<ImageButton>(R.id.imageButtonTaskEdit)
                editButton.setOnClickListener{
                    onEditClickListener?.invoke(task)
                }
                val deleteButton = itemView.findViewById<ImageButton>(R.id.imageButtonTaskDelete)
                deleteButton.setOnClickListener {
                    onDeleteClickListener?.invoke(task)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListPagingAdapter.TaskPagingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskPagingViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: TaskListPagingAdapter.TaskPagingViewHolder, position: Int) {
        val item = getItem(position)!!
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item)
    }
}