package com.muthuvarshith.nammaraste.repository

import com.muthuvarshith.nammaraste.data.ReportDao
import com.muthuvarshith.nammaraste.model.Report
import kotlinx.coroutines.flow.Flow

class ReportRepository(private val reportDao: ReportDao) {
    val allReports: Flow<List<Report>> = reportDao.getAllReports()

    suspend fun insertReport(report: Report) = reportDao.insertReport(report)
    suspend fun getReportByTicketId(ticketId: String) = reportDao.getReportByTicketId(ticketId)
    suspend fun getCount() = reportDao.getCount()
}
