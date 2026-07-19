package com.kg.yildizname.core.data.remote

import dev.gitlive.firebase.auth.FirebaseAuth

class AuthSource(
    private val auth: FirebaseAuth
) {
    suspend fun ensureSignedIn() : String?
    {
        val existingUser = auth.currentUser
        return existingUser?.uid ?: auth.signInAnonymously().user?.uid
    }
}