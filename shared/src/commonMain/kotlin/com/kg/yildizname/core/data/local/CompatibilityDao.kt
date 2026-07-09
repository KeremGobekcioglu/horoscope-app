package com.kg.yildizname.core.data.local

// data/local/CompatibilityDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompatibilityDao {
    @Query("SELECT * FROM compatibility_results WHERE id = :id")
    suspend fun getById(id: String): CompatibilityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CompatibilityEntity)
}