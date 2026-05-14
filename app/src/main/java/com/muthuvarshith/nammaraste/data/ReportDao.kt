package com.muthuvarshith.nammaraste.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muthuvarshith.nammaraste.model.Report
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report)

    @Query("SELECT * FROM reports WHERE ticketId = :ticketId LIMIT 1")
    suspend fun getReportByTicketId(ticketId: String): Report?

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT COUNT(*) FROM reports")
    suspend fun getCount(): Int
}
