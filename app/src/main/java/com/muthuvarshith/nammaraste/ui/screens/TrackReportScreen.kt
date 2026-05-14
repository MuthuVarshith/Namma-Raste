package com.muthuvarshith.nammaraste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muthuvarshith.nammaraste.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackReportScreen(
    viewModel: ReportViewModel,
    onBack: () -> Unit
) {
    var ticketIdInput by remember { mutableStateOf("") }
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Status") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = ticketIdInput,
                onValueChange = { ticketIdInput = it },
                label = { Text("Enter Ticket ID (NRR-YYYYMMDD-XXXX)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. NRR-20240520-1234") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.searchReport(ticketIdInput.trim()) },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Search Database")
            }

            Spacer(modifier = Modifier.height(32.dp))

            searchResult?.let { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "TICKET DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.weight(1f))
                            Badge(
                                containerColor = when(report.status) {
                                    "Resolved" -> Color(0xFF4CAF50)
                                    "In Progress" -> Color(0xFFFF9800)
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            ) {
                                Text(report.status, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                        }
                        
                        Text(text = report.ticketId, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp))
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        
                        DetailRow(label = "Issue Type", value = report.issueType)
                        DetailRow(label = "Severity", value = report.severity)
                        DetailRow(label = "Submitted On", value = sdf.format(Date(report.timestamp)))
                        DetailRow(label = "Reporter", value = report.reporterUsername)
                        DetailRow(label = "GPS Lat/Lng", value = "${report.latitude}, ${report.longitude}")
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Description:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = report.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } ?: run {
                if (ticketIdInput.isNotEmpty()) {
                    Text("Ticket ID not found. Please verify and try again.", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth()) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.width(100.dp))
        Text(text = value, fontSize = 14.sp)
    }
}
