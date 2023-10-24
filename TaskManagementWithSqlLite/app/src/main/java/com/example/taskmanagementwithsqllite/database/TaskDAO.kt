package com.example.taskmanagementwithsqllite.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanagementwithsqllite.model.TaskDetail
@Dao
interface TaskDAO {

    @Insert
    fun insertTask(task:TaskDetail)

    @Query("delete from `task-table` where id=:uid")
    fun deleteTask(uid:Int)

    @Query("Select*from `task-table`")
    fun getTaskList():List<TaskDetail>

    @Update
    fun updateTask(task: TaskDetail)
}