package com.kg.yildizname.core.data.remote

import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.pairId
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirestoreSource(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getReading(
        sign: ZodiacSign,
        date: String,
        period: PeriodType,
    ): FirestoreReadingDto? {
        val docId = "${sign.firestoreKey}_${date}_${period.apiKey}"
        val snapshot = firestore
            .collection("readings")
            .document(docId)
            .get()
        return if (snapshot.exists) snapshot.data<FirestoreReadingDto>() else null
    }

    suspend fun getCompatibilityResult(
        signA : ZodiacSign,
        signB : ZodiacSign
    ): CompatibilityResultDto?
    {
        val docId = pairId(signA,signB)
        val snapshot = firestore
            .collection("compatibility")
            .document(docId)
            .get()
        return if(snapshot.exists) snapshot.data<CompatibilityResultDto>() else null
    }

    suspend fun saveDeviceToken(
        uid: String, token: String
    )
    {
        firestore.collection("users")
            .document(uid)
            .set(
                // Using a raw Map instead of a @Serializable data class here.
                // Reason: FieldValue.serverTimestamp isn't real data — it's a placeholder
                // that tells Firestore "fill in the server's current time yourself."
                // kotlinx.serialization expects real typed values, so mixing this
                // placeholder into a typed DTO can break serialization. A plain Map
                // skips serialization entirely and hands the data straight to Firestore,
                // which already knows how to handle the placeholder correctly.
                data = mapOf(
                    "fcmToken" to token,
                    "lastUpdatedAt" to FieldValue.serverTimestamp
                ),
                merge = true
            )
    }
}
