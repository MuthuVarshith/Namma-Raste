package com.muthuvarshith.nammaraste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthuvarshith.nammaraste.model.Report
import com.muthuvarshith.nammaraste.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

sealed class ReportState {
    object Idle : ReportState()
    object Loading : ReportState()
    data class Success(val ticketId: String) : ReportState()
    data class Error(val message: String) : ReportState()
}

class ReportViewModel(private val repository: ReportRepository) : ViewModel() {
    
    val allReports = repository.allReports

    private val _reportState = MutableStateFlow<ReportState>(ReportState.Idle)
    val reportState: StateFlow<ReportState> = _reportState.asStateFlow()

    private val _searchResult = MutableStateFlow<Report?>(null)
    val searchResult: StateFlow<Report?> = _searchResult.asStateFlow()

    fun submitReport(
        issueType: String,
        severity: String,
        description: String,
        lat: Double,
        lng: Double,
        imagePath: String,
        reporter: String
    ) {
        viewModelScope.launch {
            _reportState.value = ReportState.Loading
            try {
                // Professional Ticket ID: NRR-YYYYMMDD-Random4
                val datePrefix = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
                val randomSuffix = Random.nextInt(1000, 9999)
                val ticketId = "NRR-$datePrefix-$randomSuffix"
                
                val report = Report(
                    ticketId = ticketId,
                    issueType = issueType,
                    severity = severity,
                    description = description,
                    latitude = lat,
                    longitude = lng,
                    timestamp = System.currentTimeMillis(),
                    imagePath = imagePath,
                    reporterUsername = reporter,
                    status = "Submitted"
                )
                repository.insertReport(report)
                _reportState.value = ReportState.Success(ticketId)
            } catch (e: Exception) {
                _reportState.value = ReportState.Error(e.message ?: "Submission failed")
            }
        }
    }

    fun searchReport(ticketId: String) {
        viewModelScope.launch {
            _searchResult.value = repository.getReportByTicketId(ticketId)
        }
    }

    fun resetState() {
        _reportState.value = ReportState.Idle
        _searchResult.value = null
    }
}
