<div align="center">

# 📖 Tilawah (Quran Recitation)

A modern, open-source Android application for Quranic listening, reading, and learning, built with **Jetpack Compose** and **Clean Architecture**.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Media3](https://img.shields.io/badge/Media3-ExoPlayer-orange.svg)](https://developer.android.com/media/media3)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

</div>

---

## 📑 Table of Contents
- [Overview](#-overview)
- [Architecture & Tech Stack](#-architecture--tech-stack)
- [Project Structure & Modules](#-project-structure--modules)
- [Features](#-features)
- [Data Sources & APIs](#-data-sources--apis)
- [Getting Started](#-getting-started)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 Overview
**Tilawah** is a comprehensive Islamic application empowering users to read the Holy Quran, listen to world-renowned reciters, stream Islamic radio, and explore authentic Hadith collections. It leverages the latest modern Android development paradigms to deliver a robust, dynamic, and seamless user experience.

---

## 🏗 Architecture & Tech Stack

The application relies on modern Android standards, adhering to **Clean Architecture** and the **MVVM (Model-View-ViewModel)** design pattern, ensuring a scalable and easily maintainable codebase.

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) featuring responsive Material 3 design and dynamic theming.
- **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/) for module scoping and loosely coupled dependencies. (See [`/di`](app/src/main/java/com/example/quranoffline/di))
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson) for building type-safe REST API clients. (See [`/data`](app/src/main/java/com/example/quranoffline/data))
- **Media Playback:** [Media3 / ExoPlayer](https://developer.android.com/media/media3) for seamless background audio streaming and lifecycle management. (See [`/media`](app/src/main/java/com/example/quranoffline/media))
- **Concurrency:** Built extensively on [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & reactive UI states with `StateFlow`.
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for type-safe routing. (See [`AppNavHost.kt`](app/src/main/java/com/example/quranoffline/AppNavHost.kt))

---

## 📂 Project Structure & Modules

The source code is modularized logically by feature packages within [`com.example.quranoffline`](app/src/main/java/com/example/quranoffline). Click any module below to dive directly into its implementation:

| Package / Module                                                  | Description | Core Files |
|:------------------------------------------------------------------| :--- | :--- |
| **[`/data`](app/src/main/java/com/example/quranoffline/data)**    | Houses API service definitions and network models mapping to external APIs. | [`Mp3QuranApi.kt`](app/src/main/java/com/example/quranoffline/data/Mp3QuranApi.kt), [`QuranService.kt`](app/src/main/java/com/example/quranoffline/data/QuranService.kt) |
| **[`/di`](app/src/main/java/com/example/quranoffline/di)**        | Dependency injection configurations utilizing Hilt to instantiate network clients and repositories. | [`AppModule.kt`](app/src/main/java/com/example/quranoffline/di/AppModule.kt), [`RepositoryModule.kt`](app/src/main/java/com/example/quranoffline/di/RepositoryModule.kt) |
| **[`/media`](app/src/main/java/com/example/quranoffline/media)**  | The core media engine built with AndroidX Media3, managing player state, foreground services, and view-model bridging. | [`MediaPlaybackService.kt`](app/src/main/java/com/example/quranoffline/media/MediaPlaybackService.kt), [`MediaViewModel.kt`](app/src/main/java/com/example/quranoffline/media/MediaViewModel.kt) |
| **[`/ui`](app/src/main/java/com/example/quranoffline/ui)**        | The presentation layer housing all UI screens, custom composables, components, and MVVM integration. | [`home`](app/src/main/java/com/example/quranoffline/ui/home), [`reciters`](app/src/main/java/com/example/quranoffline/ui/reciters), [`components`](app/src/main/java/com/example/quranoffline/ui/components) |
| **[`/util`](app/src/main/java/com/example/quranoffline/util)**    | General helper utilities, including localization and extensions. | [`LocaleHelper.kt`](app/src/main/java/com/example/quranoffline/util/LocaleHelper.kt), [`HadithLocalizationHelper.kt`](app/src/main/java/com/example/quranoffline/util/HadithLocalizationHelper.kt) |
| **`Root`**                                                        | Root entry points, Main Activity, and navigation graphing. | [`MainActivity.kt`](app/src/main/java/com/example/quranoffline/MainActivity.kt), [`AppNavHost.kt`](app/src/main/java/com/example/quranoffline/AppNavHost.kt) |

---

## ✨ Features

- **📖 Quran Scripts:** Read the Holy Quran directly in the app with high-quality scripts mapped dynamically. (See [`ui/ChapterScript`](app/src/main/java/com/example/quranoffline/ui/ChapterScript))
- **🎧 Recitations:** Listen to beautiful recitations from various world-renowned reciters integrated with background playback. (See [`ui/reciters`](app/src/main/java/com/example/quranoffline/ui/reciters))
- **📻 Islamic Radio:** Stream live Islamic radio stations with a modernized, transparent UI. (See [`ui/Radio`](app/src/main/java/com/example/quranoffline/ui/Radio))
- **📚 Hadith Collections:** Browse and gracefully search through extensive, authentic Hadith books. (See [`ui/HadithScript`](app/src/main/java/com/example/quranoffline/ui/HadithScript))
- **🎵 Interactive Media Playback:** Advanced media controller with an interactive seek bar supporting horizontal swipe gestures for precise seek, rewind, and fast-forward controls.
- **🌙 Premium UI & Theming:** Modern Material 3 design with a unified purple color theme (`BrandPrimary`), providing a consistent experience in both Light and Dark modes.
- **🖼 Optimized Visuals:** Enhanced image display and layout centering to ensure a high-quality, distortion-free experience across all devices.
- **🌍 Full RTL & Localization Support:** Optimized for both Arabic and English languages with seamless Right-to-Left (RTL) layout support, including localized media notifications and UI components. (See [`util/HadithLocalizationHelper.kt`](app/src/main/java/com/example/quranoffline/util/HadithLocalizationHelper.kt))

---

## 🌐 Data Sources & APIs

The application integrates with reliable, high-quality open-source APIs to fetch its rich content dynamically:
- **Quran Text & Scripts:** [quranapi.pages.dev](https://quranapi.pages.dev)
- **Audio & Recitations:** [mp3quran.net](https://mp3quran.net/eng/api)
- **Translations & Tafseer:** [api.quran-tafseer.com](http://api.quran-tafseer.com/en/docs/)
- **Hadith Databases:** [hadithapi.com](https://hadithapi.com)

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/githussein/free-media-player
   ```
2. **Open in Android Studio:** Ensure you're running the latest stable or beta version of Android Studio (Ladybug or newer) supporting the current Kotlin & Compose versions.
3. **Build & Run:** Let Gradle sync all dependencies, select your connected device or emulator, and click **Run**.

---

## 🤝 Contributing

Tilawah is an **open-source project**, and we strongly believe in the power of community! **Any developer is welcome and encouraged to contribute** to the repository to enhance features, fix bugs, or improve documentation. 

Whether you want to add new reciters, improve the UI, or optimize performance, your help is appreciated. Feel free to open issues, discuss ideas, or submit pull requests. Let's work together to make learning and listening to the Quran more accessible to everyone.

## 📄 License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
