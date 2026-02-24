package com.clocked.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    /** Observe all shifts in a given month, ordered by date then start time. */
    @Query("SELECT * FROM shifts WHERE date LIKE :yearMonthPrefix || '%' ORDER BY date, startTime")
    fun getShiftsForMonth(yearMonthPrefix: String): Flow<List<ShiftEntity>>

    /** One-shot fetch — used by import to check for duplicates. */
    @Query("SELECT * FROM shifts WHERE date = :date AND startTime = :startTime LIMIT 1")
    suspend fun findByDateAndStart(date: String, startTime: String): ShiftEntity?

    @Query("SELECT * FROM shifts WHERE id = :id")
    suspend fun getById(id: Long): ShiftEntity?

    /** Insert, silently skip duplicates (same date + startTime index). */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(shifts: List<ShiftEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shift: ShiftEntity): Long

    @Update
    suspend fun update(shift: ShiftEntity)

    @Delete
    suspend fun delete(shift: ShiftEntity)

    /** Prune months older than the retention cutoff. E.g. pass "2025-12" to delete everything before Jan. */
    @Query("DELETE FROM shifts WHERE date < :cutoffDate")
    suspend fun deleteOlderThan(cutoffDate: String)

    @Query("DELETE FROM shifts")
    suspend fun deleteAll()
}
