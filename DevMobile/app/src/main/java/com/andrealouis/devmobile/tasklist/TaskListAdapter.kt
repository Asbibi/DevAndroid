package com.andrealouis.devmobile.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrealouis.devmobile.R
import kotlin.properties.Delegates

class TaskListAdapter () : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    var onEditClickListener: ((Task) -> Unit)? = null
    var onDeleteClickListener: ((Task) -> Unit)? = null
    // val taskList: LiveData<List<Task>> = _taskList

    //var taskList: List<Task> = emptyList() // = mutableListOf<Task>()
    var taskList: List<Task> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                var textView = itemView.findViewById<TextView>(R.id.task_title)
                textView.text = task.title
                val editButton = itemView.findViewById<ImageButton>(R.id.imageButtonTaskEdit)
                editButton.setOnClickListener{
                    onEditClickListener?.invoke(task)
                }
                val deleteButton = itemView.findViewById<ImageButton>(R.id.imageButtonTaskDelete)
                deleteButton.setOnClickListener{
                    onDeleteClickListener?.invoke(task)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }


}