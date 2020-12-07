package com.andrealouis.devmobile.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.network.Api
import com.andrealouis.devmobile.network.TasksRepository
import com.andrealouis.devmobile.task.TaskActivity
import com.andrealouis.devmobile.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    //private val taskList = listOf("Task 1", "Task 2", "Task 3")
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    val taskListAdapter = TaskListAdapter()
    private val tasksRepository = TasksRepository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pour une [RecyclerView] ayant l'id "recycler_view":
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        taskListAdapter.onEditClickListener = { task ->
            //taskListAdapter.notifyItemRemoved(taskList.indexOf(task))
            //taskList.remove(task)
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        taskListAdapter.onDeleteClickListener = { task ->
            taskListAdapter.notifyItemRemoved(taskList.indexOf(task))
            taskList.remove(task)
        }
        recyclerView.adapter = taskListAdapter
        val button = view.findViewById<FloatingActionButton>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskListAdapter.taskList.clear()
            taskListAdapter.taskList.addAll(it)
            taskListAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        // TODO : Chercher si la tache existe déjà (cad on renvoie une tache modifiée ou on en crée une nouvelle
        taskList.add(taskList.size, task)
        taskListAdapter.notifyItemInserted(taskList.size)
    }

    override fun onResume(){
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val my_text_view = view?.findViewById<TextView>(R.id.userInfo_textView)
            my_text_view?.text = "${userInfo.firstName} ${userInfo.lastName}"
            tasksRepository.refresh()
        }
    }
}

