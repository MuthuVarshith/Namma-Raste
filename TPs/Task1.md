# Namma-Raste Reporter App Exercise

## Objective
Complete the implementation of the "Namma-Raste Reporter" app, focusing on civic issue reporting and status tracking using Jetpack Compose and MVVM.

## Key Concepts
- MVVM Architecture (Model-View-ViewModel)
- Jetpack Compose UI
- Room Database for local persistence
- CameraX for image capture
- FusedLocationProvider for GPS tracking

## Project Structure
- `ui`: Screens (Login, Home, Report, Track)
- `data`: Room Database and DAOs
- `model`: Entities (User, Report)
- `repository`: Data handling
- `viewmodel`: Business logic and state management

## Implementation Tasks

### 1. Authentication
- Implement login and registration logic in `AuthViewModel`.
- Ensure secure password handling and unique username validation.

### 2. Issue Reporting
- Use `ActivityResultContracts.TakePicture()` to capture photos.
- Fetch real-time GPS coordinates using `FusedLocationProviderClient`.
- Generate unique Ticket IDs (e.g., NRR-1001) in `ReportViewModel`.

### 3. Tracking System
- Implement a search feature by Ticket ID in `TrackReportScreen`.
- Display detailed report information and current status.

### 4. Recent Reports
- Display a list of all submitted reports using `LazyColumn` and `ReportCard`.

## Expected Outcome
A fully functional app that allows citizens to report road issues with photo evidence and GPS locations, providing a unique tracking ID for each submission.
