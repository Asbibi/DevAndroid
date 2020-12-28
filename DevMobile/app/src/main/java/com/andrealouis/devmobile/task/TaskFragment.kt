package com.andrealouis.devmobile.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrealouis.devmobile.R
import java.util.*


class TaskFragment : Fragment() {
    companion object {
        const val EDIT_TASK_REQUEST_CODE = 667
        const val ADD_TASK_REQUEST_CODE = 666
        const val ADD_TASK_KEY = "newTask"
        const val EDIT_TASK_KEY = "editedTask"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val taskModified = intent.getSerializableExtra(TaskActivity.TASK_KEY) as? Task
        val taskModified = findNavController().previousBackStackEntry?.savedStateHandle?.get(EDIT_TASK_KEY) as? Task
        var edition = taskModified != null
        if (edition){
            view?.findViewById<EditText>(R.id.editTextTaskTitle).setText(taskModified?.title)
            view?.findViewById<EditText>(R.id.editTextTaskDescription).setText(taskModified?.description)
        }

        val button = view?.findViewById<Button>(R.id.taskValidateButton)
        button.setOnClickListener{
            val title = view?.findViewById<EditText>(R.id.editTextTaskTitle).text.toString()
            if (title != ""){

                val description = view?.findViewById<EditText>(R.id.editTextTaskDescription).text.toString()
                val newTask = Task(id = taskModified?.id ?: UUID.randomUUID().toString(),
                    title = title,
                    description = description)

                if(edition)
                    findNavController().currentBackStackEntry?.savedStateHandle?.set(EDIT_TASK_KEY, newTask)
                else
                    findNavController().currentBackStackEntry?.savedStateHandle?.set(ADD_TASK_KEY, newTask)

                findNavController().navigate(R.id.action_taskFragment_to_taskListFragment)
            }
            else{
                Toast.makeText(context, "Nom vide", Toast.LENGTH_LONG).show()
            }
        }
    }

}