# Namma Raste (Our Road) Reporter 🛣️

**Namma Raste** is a modern Android application built to empower citizens to report and track civic infrastructure issues like potholes, broken streetlights, and drainage problems. Built with Jetpack Compose and a local-first architecture.

## 🚀 Features

- **Issue Reporting:** Capture photos of issues directly within the app using CameraX.
- **Smart Location:** Automatically attaches GPS coordinates to every report for precise mapping.
- **Local Storage:** Uses Room Database for high-performance, offline-capable data persistence.
- **Search & Track:** Easily find reports using a unique Ticket ID system.
- **User Authentication:** Secure login and registration system to maintain report integrity.
- **Modern UI:** Clean, responsive interface built entirely with Jetpack Compose and Material 3.

## 🛠️ Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/) (2.0.0)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Database:** [Room SQLite](https://developer.android.com/training/data-storage/room)
- **Media:** [CameraX](https://developer.android.com/training/camerax) for image capture
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **Location:** [Google Play Services Location](https://developers.google.com/android/guides/setup)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Management:** Version Catalogs (`libs.versions.toml`)

## 📂 Project Structure

```text
com.muthuvarshith.nammaraste/
├── data/          # Room Database, DAOs (UserDao, ReportDao)
├── model/         # Data Entities (User, Report)
├── repository/    # Data logic and abstraction
├── ui/            # Compose Screens, Navigation, and Theme
└── viewmodel/     # UI State and Business Logic
```

## 🏗️ Getting Started

1. **Clone the repo:**
   ```bash
   git clone https://github.com/muthuvarshith/Namma-Raste.git
   ```
2. **Open in Android Studio:**
   Open the folder as an existing Android Studio project.
3. **Build & Run:**
   Ensure you have an Android device or emulator running (API 24+). Hit **Run**.

## 🔒 Privacy & Permissions

The app requires the following permissions to function correctly:
- `CAMERA`: To capture images of civic issues.
- `ACCESS_FINE_LOCATION`: To tag reports with precise GPS coordinates.
- `READ_EXTERNAL_STORAGE`: To handle image URI processing.

---
Developed by **Muthu Varshith**
