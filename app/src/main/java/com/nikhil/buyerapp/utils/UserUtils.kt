package com.nikhil.buyerapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object UserUtils {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // A Reusable Suspend Function
    // Returns: The Name String (or "Unknown" if failed)
    suspend fun fetchCurrentUserName(): String {
        return withContext(Dispatchers.IO) {
            val uid = auth.currentUser?.uid ?: return@withContext "Unknown"

            // CHECK 1: Firebase Auth Profile (Fastest)
            val authName = auth.currentUser?.displayName
            if (!authName.isNullOrBlank()) {
                return@withContext authName
            }

            // CHECK 2: Firestore Database (Slower but reliable)
            try {
                val document = db.collection("Users").document(uid).get().await()
                // Change "fullName" to whatever your field is named in Firestore
                return@withContext document.getString("fullName")
                    ?: document.getString("name")
                    ?: "Unknown Client"
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext "Unknown Client"
            }
        }
    }
}