package com.andrealouis.devmobile.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.andrealouis.devmobile.R
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val EDIT_TASK_REQUEST_CODE = 667
        const val ADD_TASK_REQUEST_CODE = 666
        const val TASK_KEY = "newTask"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val taskModified = intent.getSerializableExtra(Companion.TASK_KEY) as? Task
        if (taskModified != null){
            findViewById<EditText>(R.id.editTextTaskTitle).setText(taskModified?.title)
            findViewById<EditText>(R.id.editTextTaskDescription).setText(taskModified?.description)
        }

        val button = findViewById<Button>(R.id.taskValidateButton)
        button.setOnClickListener{
            val title = findViewById<EditText>(R.id.editTextTaskTitle).text.toString()
            val description = findViewById<EditText>(R.id.editTextTaskDescription).text.toString()
            val newTask = Task(id = taskModified?.id ?: UUID.randomUUID().toString(),
                    title = title,
                    description = description)

            intent.putExtra(Companion.TASK_KEY, newTask)
            setResult(RESULT_OK, intent)

            finish()
            //val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            //taskList.add(taskList.size, task)
            //taskListAdapter.notifyItemInserted(taskList.size)
        }
    }
}