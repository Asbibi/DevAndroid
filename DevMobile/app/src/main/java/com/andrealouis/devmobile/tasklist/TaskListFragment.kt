package com.andrealouis.devmobile.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.task.Task
import com.andrealouis.devmobile.task.TaskActivity
import com.andrealouis.devmobile.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.andrealouis.devmobile.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.andrealouis.devmobile.userinfo.UserInfo
import com.andrealouis.devmobile.userinfo.UserInfoActivity
import com.andrealouis.devmobile.userinfo.UserInfoActivity.Companion.EDIT_USER_INFO_REQUEST_CODE
import com.andrealouis.devmobile.userinfo.UserInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
class EditTask : ActivityResultContract<Task, Task>() {
    override fun createIntent(context: Context, inputTask : Task): Intent{}
    override fun parseResult(resultCode: Int, intent: Intent?):Task {}
}*/

class TaskListFragment : Fragment() {

    //private val taskList = listOf("Task 1", "Task 2", "Task 3")
    /*private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/
    val taskListAdapter = TaskListAdapter()
    //private val tasksRepository = TasksRepository()
    private val viewModel : TaskListViewModel by viewModels()
    private val userInfoViewModel : UserInfoViewModel by viewModels()

    /*
    val editTaskCallbackForResult = registerForActivityResult(EditTask()) {
        val task = EditTask.parseResult(0,)
        viewModel.editTask(task)
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { task:Task? ->
        // Handle the returned Uri
    }
    */

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
            /*taskListAdapter.notifyItemRemoved(taskList.indexOf(task))
            taskList.remove(task)*/
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            //editTaskCallbackForResult.launch(task)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }
        taskListAdapter.onDeleteClickListener = { task ->
            //taskListAdapter.notifyItemRemoved(taskList.indexOf(task))
            //taskList.remove(task)
            viewModel.deleteTask(task)
            //lifecycleScope.launch { tasksRepository.deleteTask(task)}
        }
        recyclerView.adapter = taskListAdapter
        val button = view.findViewById<FloatingActionButton>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        viewModel.taskList.observe(viewLifecycleOwner, Observer { newList ->
            taskListAdapter.taskList = newList.orEmpty()
            //taskListAdapter.notifyDataSetChanged()
        })
        val userImageView = view?.findViewById<ImageView>(R.id.userInfo_imageView)
        userImageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra(UserInfoActivity.USER_INFO_KEY, userInfoViewModel.userInfo.value)
            startActivityForResult(intent,EDIT_USER_INFO_REQUEST_CODE)
        }
        /*tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskListAdapter.taskList.clear()
            taskListAdapter.taskList.addAll(it)
            taskListAdapter.notifyDataSetChanged()
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
            viewModel.addTask(task)
            //lifecycleScope.launch { tasksRepository.addTask(task!!)}
        }
        else if (requestCode == EDIT_TASK_REQUEST_CODE) {
            val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
            viewModel.editTask(task)
            //lifecycleScope.launch { tasksRepository.editTask(task!!)}
        }
        else if (requestCode == EDIT_USER_INFO_REQUEST_CODE){
            val userInfo = data!!.getSerializableExtra(UserInfoActivity.USER_INFO_KEY) as UserInfo
            userInfoViewModel.editUserInfo(userInfo)
        }
        //taskList.add(taskList.size, task)
        //taskListAdapter.notifyItemInserted(taskList.size)
    }

    override fun onResume(){
        super.onResume()
        userInfoViewModel.loadUserInfo()
        userInfoViewModel.userInfo.observe(viewLifecycleOwner, Observer { newInfo ->
            val userInfo = newInfo
            val my_text_view = view?.findViewById<TextView>(R.id.userInfo_textView)
            val myImageView = view?.findViewById<ImageView>(R.id.userInfo_imageView)
            my_text_view?.text = "${userInfo?.firstName} ${userInfo?.lastName}"
            myImageView?.load(userInfo?.avatar) {    //"https://goo.gl/gEgYUd"
                transformations(CircleCropTransformation())
            }
        })
        viewModel.loadTasks()
    }
}

