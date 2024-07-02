package com.itssvkv.todolist.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itssvkv.todolist.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TasksDao
}
