package com.andrealouis.devmobile.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.task.Task
import com.andrealouis.devmobile.task.TaskFragment
import com.andrealouis.devmobile.userinfo.UserInfo
import com.andrealouis.devmobile.userinfo.UserInfoFragment
import com.andrealouis.devmobile.userinfo.UserInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TaskListPagingFragment : Fragment() {
    val pagingAdapter = TaskListPagingAdapter(TaskListPagingComparator)
    val taskListViewModel by viewModels<TaskListPagingViewModel>()
    private val userInfoViewModel : UserInfoViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list_paging, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = pagingAdapter

        lifecycleScope.launch {
            taskListViewModel.flow.collectLatest { pagingData ->

                pagingAdapter.submitData(pagingData)
            }
        }


        pagingAdapter.onEditClickListener = { task ->
            findNavController().currentBackStackEntry?.savedStateHandle?.set(TaskFragment.EDIT_TASK_KEY, task)
            findNavController().navigate(R.id.action_taskListPagingFragment_to_taskFragment)
        }
        pagingAdapter.onDeleteClickListener = { task ->
            taskListViewModel.deleteTask(task)
        }
        val addButton = view.findViewById<FloatingActionButton>(R.id.button)
        addButton.setOnClickListener{
            findNavController().currentBackStackEntry?.savedStateHandle?.set(TaskFragment.EDIT_TASK_KEY, null)
            findNavController().navigate(R.id.action_taskListPagingFragment_to_taskFragment)
        }
        val userImageView = view?.findViewById<ImageView>(R.id.userInfo_imageView)
        userImageView.setOnClickListener {
            findNavController().currentBackStackEntry?.savedStateHandle?.set(UserInfoFragment.USER_INFO_KEY, userInfoViewModel.userInfo.value)
            findNavController().navigate(R.id.action_taskListPagingFragment_to_userInfoFragment)
        }

        findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<Task>(TaskFragment.ADD_TASK_KEY)?.observe(viewLifecycleOwner) { task ->
            taskListViewModel.addTask(task)
        }
        findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<Task>(TaskFragment.EDIT_TASK_KEY)?.observe(viewLifecycleOwner) { editedtask ->
            if (editedtask != null)
                taskListViewModel.editTask(editedtask)
        }
        findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<UserInfo>(UserInfoFragment.USER_INFO_KEY)?.observe(viewLifecycleOwner) { userInfo ->
            userInfoViewModel.editUserInfo(userInfo)
        }
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
        //taskListViewModel.loadTasks()
    }
}