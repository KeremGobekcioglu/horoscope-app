package com.kg.yildizname.core.data.remote

import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.pairId
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
}
