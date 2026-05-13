# 🚧 Namma-Raste Reporter - Civic Issue Reporting App

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![Compose Version](https://img.shields.io/badge/Jetpack%20Compose-1.10.0-brightgreen)](https://developer.android.com/jetpack/compose)

Namma-Raste Reporter is a complete Android infrastructure issue reporting application built using Kotlin and Jetpack Compose. It allows citizens to report civic issues like potholes, broken streetlights, and road damage directly to the authorities with photo evidence and GPS location.

## Features ✨
- **Issue Reporting**: Capture images using CameraX, select issue type and severity.
- **Automatic Data Collection**: Automatically fetches GPS location and generates timestamps.
- **Unique Ticket IDs**: Generates unique Ticket IDs (e.g., NRR-1001) for every report.
- **Status Tracking**: Search and track the status of reported issues using Ticket IDs.
- **Authentication**: Simple login/register system to prevent anonymous reports.
- **Local Persistence**: Saves all reports and user data locally using Room Database.
- **Modern UI**: Clean Material3 UI with a green civic/government theme.

## Architecture 🏗️
The project follows **MVVM (Model-View-ViewModel)** architecture with the following package structure:
- `ui`: Composable screens and theme.
- `data`: Room Database, DAOs.
- `model`: Data entities (User, Report).
- `repository`: Data handling logic.
- `viewmodel`: UI state management.

## Technologies Used 🛠️
- **Jetpack Compose** - UI toolkit
- **Kotlin Coroutines** - Asynchronous operations
- **Room Database** - Local storage
- **CameraX** - Image capture
- **FusedLocationProviderClient** - GPS location
- **Navigation Compose** - App navigation
- **Material Design 3** - UI components

## Installation & Setup 🚀

### Prerequisites
- Android Studio Ladybug or newer
- Android SDK 33+
- Kotlin 2.0.0

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/muthuvarshith/Namma-Raste-Reporter.git
   ```
2. **Open in Android Studio**
    - Select "Open" and choose the project directory.

3. **Build and Run**
    - Ensure a device or emulator is connected.
    - Click the "Run" button.

## Contact 📧
For questions or suggestions:

**Project Developer**: Muthu Varshith  
**Email**: muthuvarshith290@gmail.com
