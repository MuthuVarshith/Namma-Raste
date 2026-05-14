package com.muthuvarshith.nammaraste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.muthuvarshith.nammaraste.data.AppDatabase
import com.muthuvarshith.nammaraste.repository.ReportRepository
import com.muthuvarshith.nammaraste.repository.UserRepository
import com.muthuvarshith.nammaraste.ui.NavGraph
import com.muthuvarshith.nammaraste.ui.theme.NammaRasteReporterTheme
import com.muthuvarshith.nammaraste.viewmodel.AuthViewModel
import com.muthuvarshith.nammaraste.viewmodel.ReportViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())
        val reportRepository = ReportRepository(database.reportDao())

        enableEdgeToEdge()
        setContent {
            NammaRasteReporterTheme {
                val navController = rememberNavController()
                
                // Dependency Injection using ViewModel Factories
                val authViewModel: AuthViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return AuthViewModel(userRepository) as T
                        }
                    }
                )
                
                val reportViewModel: ReportViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ReportViewModel(reportRepository) as T
                        }
                    }
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        reportViewModel = reportViewModel
                    )
                }
            }
        }
    }
}
