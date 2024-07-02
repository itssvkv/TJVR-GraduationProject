package com.itssvkv.todolist.repository.room

import com.itssvkv.todolist.data.local.room.TasksDao
import com.itssvkv.todolist.data.local.room.TasksDatabase
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val db: TasksDatabase
) : RoomRepository {
}
