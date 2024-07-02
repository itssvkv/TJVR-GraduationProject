package com.itssvkv.todolist.repository.network

import com.itssvkv.todolist.data.network.ApiCalls
import javax.inject.Inject

class ApiCallsRepositoryImpl @Inject constructor(
    private val apiCalls: ApiCalls
): ApiCallsRepository {

}