package com.itssvkv.todolist.model

data class UserDetails(
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val photo: String = "",
    val password: String = "",
    val address: String = "",
    val aboutYou: String = "",
    val diagnosis: String = "Normal"
)
