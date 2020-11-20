package com.example.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao{
    @Insert
    suspend fun insert(todoModel : ToDoModel) : Long

    @Query("SELECT * FROM ToDoModel WHERE isFinished != -1")
    fun getTask() : LiveData<List<ToDoModel>>

    @Query("UPDATE ToDoModel SET isFinished = 1 WHERE id = :uid")
    fun finishTask(uid : Long)

    @Query("DELETE FROM ToDoModel WHERE id = :uid")
    fun deleteTask(uid:Long)
}