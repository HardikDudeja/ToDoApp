package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db";

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    // these two things are required for date picker dialog
    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private val labels = arrayListOf("Personal", "Business", "Shopping")
    var finalDate = 0L
    var finalTime = 0L

    val db by lazy{
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        tiDateEdit.setOnClickListener(this)
        tiTimeEdit.setOnClickListener(this)

        setupSpinner()

        btnSave.setOnClickListener(this)
    }

    private fun setupSpinner() {
        labels.sort()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)
        spinnerCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tiDateEdit -> {
                setListener()
            }
            R.id.tiTimeEdit -> {
                setTimeListener()
            }
            R.id.btnSave -> {
                saveTodo()
            }
        }
    }

    private fun saveTodo(){
        val title = taskTitle.text.toString()
        val description = taskDetails.text.toString()
        val category = spinnerCategory.selectedItem.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insert(
                        ToDoModel(title,
                                description,
                                category,
                                finalDate,
                                finalTime)
                )
            }
            finish() // this activity will get finished
        }
    }

    private fun setTimeListener() {
        timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour : Int, min : Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(this, timeSetListener,
        myCalendar.get(Calendar.HOUR_OF_DAY),
        myCalendar.get(Calendar.MINUTE),
        false)

        timePickerDialog.show()
    }

    private fun updateTime() {
        val myFormat = "h:mm a" // time format
        val stf = SimpleDateFormat(myFormat)
        tiTimeEdit.setText(stf.format(myCalendar.time))
        finalTime = myCalendar.time.time
    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener{_ : DatePicker, year : Int, month : Int, dayOfMonth : Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        } // required details such as year month day

        val datePickerDialog = DatePickerDialog(this, dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

//        updateDate() // it is called only once
    }

    private fun updateDate() {
        val myFormat = "EEE, d MMM yyyy" // this is the required date format
        val sdf = SimpleDateFormat(myFormat)
        tiDateEdit.setText(sdf.format(myCalendar.time))
        tilTimeEdit.visibility = View.VISIBLE
        finalDate = myCalendar.time.time
    }
}