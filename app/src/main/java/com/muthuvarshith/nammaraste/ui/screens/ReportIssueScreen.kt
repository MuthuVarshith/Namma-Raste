package com.muthuvarshith.nammaraste.ui.screens

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add // Added missing import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationServices
import com.muthuvarshith.nammaraste.viewmodel.AuthViewModel
import com.muthuvarshith.nammaraste.viewmodel.ReportState
import com.muthuvarshith.nammaraste.viewmodel.ReportViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val reportState by reportViewModel.reportState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImagePath by remember { mutableStateOf("") }
    var issueType by remember { mutableStateOf("Pothole") }
    var severity by remember { mutableStateOf("Medium") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var showCamera by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var generatedTicketId by remember { mutableStateOf("") }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true &&
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            // Permissions granted
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    latitude = loc.latitude
                    longitude = loc.longitude
                }
            }
        } catch (e: SecurityException) {
            Log.e("Location", "Permission denied", e)
        }
    }

    if (showCamera) {
        CameraPreview(
            onImageCaptured = { uri, path ->
                capturedImageUri = uri
                capturedImagePath = path
                showCamera = false
            },
            onClose = { showCamera = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Report Issue") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
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
                // Image Section
                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (capturedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(capturedImageUri),
                            contentDescription = "Captured Issue",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Button(onClick = { showCamera = true }) {
                                Text("Capture Photo")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Issue Type Selection
                Text("Select Issue Type", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = issueType == "Pothole", onClick = { issueType = "Pothole" })
                    Text("Pothole")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = issueType == "Broken Streetlight", onClick = { issueType = "Broken Streetlight" })
                    Text("Broken Streetlight")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Severity Selection
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = severity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Severity") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Low", "Medium", "High").forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    severity = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("GPS Location", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Lat: $latitude, Lng: $longitude", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        reportViewModel.submitReport(
                            issueType = issueType,
                            severity = severity,
                            description = description,
                            lat = latitude,
                            lng = longitude,
                            imagePath = capturedImagePath,
                            reporter = currentUser?.username ?: "Unknown" // Fixed: Changed .name to .username
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = capturedImagePath.isNotEmpty() && reportState !is ReportState.Loading
                ) {
                    if (reportState is ReportState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Submit Report", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Report Submitted Successfully") },
            text = {
                Column {
                    Text("Ticket ID: ", fontWeight = FontWeight.Bold)
                    Text(generatedTicketId, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: Submitted")
                    Text("Issue: $issueType")
                }
            },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    reportViewModel.resetState()
                    onBack()
                }) {
                    Text("Return to Home")
                }
            }
        )
    }

    LaunchedEffect(reportState) {
        if (reportState is ReportState.Success) {
            generatedTicketId = (reportState as ReportState.Success).ticketId
            showSuccessDialog = true
        }
    }
}

@Composable
fun CameraPreview(
    onImageCaptured: (Uri, String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        ) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Use case binding failed", e)
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        FloatingActionButton(
            onClick = {
                val file = File(
                    context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
                    "NRR_${System.currentTimeMillis()}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            onImageCaptured(Uri.fromFile(file), file.absolutePath)
                        }
                        override fun onError(exc: ImageCaptureException) {
                            Log.e("CameraPreview", "Photo capture failed: ${exc.message}", exc)
                        }
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add, // Explicitly named parameter fixes ambiguity
                contentDescription = "Capture",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
