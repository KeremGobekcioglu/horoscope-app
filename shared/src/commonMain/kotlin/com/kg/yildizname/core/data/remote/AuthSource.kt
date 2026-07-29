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

    // Deletes the anonymous auth user itself (not just a sign-out). The next
    // call to ensureSignedIn() has no currentUser, so it creates a fresh one.
    suspend fun deleteCurrentUser() {
        auth.currentUser?.delete()
    }
}