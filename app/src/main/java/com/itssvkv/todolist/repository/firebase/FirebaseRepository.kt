package com.itssvkv.todolist.repository.firebaseimport com.google.firebase.firestore.DocumentReferenceinterface FirebaseRepository {    fun currentUserId(): String?    suspend fun currentUserDetails(): DocumentReference}