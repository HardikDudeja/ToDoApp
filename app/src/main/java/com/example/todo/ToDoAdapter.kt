package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter(private val list: List<ToDoModel>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // extends default view holder
        fun bind(toDoModel: ToDoModel){
            with(itemView){
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                tvTitle.text = toDoModel.title
                tvTaskSubtitle.text = toDoModel.description
                tvCategory.text = toDoModel.category
                updateTime(toDoModel.time)
                updateDate(toDoModel.date)
            }
        }
        private fun updateTime(time : Long) {
            val myFormat = "h:mm a"
            val sdf = SimpleDateFormat(myFormat)
            itemView.tvTime.text = sdf.format(time)
        }

        private fun updateDate(date : Long){
            val myFormat = "EEE, d MMM yyyy" // this is the required date format
            val sdf = SimpleDateFormat(myFormat)
            itemView.tvDate.text = sdf.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false))
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}