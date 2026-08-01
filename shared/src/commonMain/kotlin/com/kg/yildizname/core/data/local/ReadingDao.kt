package com.kg.yildizname.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings WHERE sign = :sign AND period = :period AND date = :date LIMIT 1")
    suspend fun getReading(sign: String, period: String, date: String): ReadingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReading(reading: ReadingEntity)

    @Query("DELETE FROM readings WHERE cachedAt < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)

    @Query("DELETE FROM readings")
    suspend fun deleteAll()
}
