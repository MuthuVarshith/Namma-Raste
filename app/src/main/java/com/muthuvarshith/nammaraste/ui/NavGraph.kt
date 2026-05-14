package com.muthuvarshith.nammaraste.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muthuvarshith.nammaraste.ui.screens.*
import com.muthuvarshith.nammaraste.viewmodel.AuthViewModel
import com.muthuvarshith.nammaraste.viewmodel.ReportViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ReportIssue : Screen("report_issue")
    object TrackReport : Screen("track_report")
    object RecentReports : Screen("recent_reports")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                authViewModel = authViewModel,
                onReportIssueClick = { navController.navigate(Screen.ReportIssue.route) },
                onTrackReportClick = { navController.navigate(Screen.TrackReport.route) },
                onRecentReportsClick = { navController.navigate(Screen.RecentReports.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ReportIssue.route) {
            ReportIssueScreen(
                authViewModel = authViewModel,
                reportViewModel = reportViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.TrackReport.route) {
            TrackReportScreen(
                viewModel = reportViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.RecentReports.route) {
            RecentReportsScreen(
                viewModel = reportViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
