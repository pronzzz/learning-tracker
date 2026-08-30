# Personal Learning Tracker

An Android application built to track personal learning progression. Optimized for Samsung One UI, featuring a clean, AMOLED-friendly "liquid glass" design, widgets, and detailed tracking tools for any topic you are studying.

## Features

- **Topics & Projects**: Organize your learning into high-level Topics (e.g. "Robotics", "Japanese") and actionable Projects (e.g. "Learn ROS2", "N5 Japanese").
- **Detailed Tracking**: Track specific Tasks (Checklists), Resources (Links), and chronological Notes per Project.
- **Time Tracking (Study Sessions)**: A built-in timer to track hours and minutes spent on each project.
- **Home Screen Widget**: A Jetpack Glance widget that displays your most active projects and progress directly on your Android launcher.
- **Liquid Glass UI**: Styled with Samsung One UI aesthetics—large borders, deep AMOLED blacks, and semi-transparent glass cards.

## Screenshots
*(Add screenshots of the Dashboard, Project Details, and Home Screen Widget here)*

## Architecture

- **UI**: Jetpack Compose, Navigation Compose (Navigation 3).
- **Architecture**: MVVM (Model-View-ViewModel).
- **Local Database**: Room DB (SQLite) powered by KSP.
- **Widgets**: Jetpack Glance.
- **Language**: Kotlin (2.1.0).

## Project Setup & Build

This project uses modern Gradle configurations (AGP 9+). Due to AGP 9 incompatibilities with Hilt, the app uses a manual Dependency Injection service locator pattern (`LearningTrackerApplication.kt`).

To build the project:
1. Clone the repository.
2. Open in Android Studio.
3. Build the APK:
```bash
./gradlew assembleDebug
```
To generate a release build:
```bash
./gradlew assembleRelease
```

## Contributing

Contributions are welcome! Please follow these steps:
1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

Please ensure all new UI components follow the existing "Liquid Glass" theme guidelines in `Theme.kt`.

## License

Distributed under the MIT License. See `LICENSE` for more information.
