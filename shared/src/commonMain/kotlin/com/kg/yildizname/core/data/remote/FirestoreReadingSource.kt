package com.kg.yildizname.core.data.remote

import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirestoreReadingSource(
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
}
