# Tilawah (Quran Recitation)

A modern, open-source Android application for Quranic listening, built with **Jetpack Compose** and **Clean Architecture**.

## ✨ Features

- **📖 Quran Scripts:** Read the Holy Quran with high-quality scripts and translations.
- **🎧 Recitations:** Listen to beautiful recitations from various world-renowned reciters.
- **📻 Islamic Radio:** Stream live Islamic radio stations directly within the app.
- **📚 Hadith Collections:** Browse and search through authentic Hadith books.
- **🎵 Media Playback:** Integrated media controller with background playback support using **Media3 ExoPlayer**.
- **🌙 Dynamic Theming:** Full support for Material 3, including light/dark modes and dynamic colors.
- **📱 Responsive UI:** Optimized for a smooth experience across different Android devices.

## 🛠️ Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM with Clean Architecture principles.
- **Dependency Injection:** [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson)
- **Media:** [Media3 (ExoPlayer)](https://developer.android.com/guide/topics/media/media3) for audio streaming and local playback.
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) (Type-safe navigation).
- **Concurrency:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

## 🌐 Data Sources & Resources

The app integrates several high-quality APIs to provide its content:
- **Quran Scripts:** [quranapi.pages.dev](https://quranapi.pages.dev)
- **Recitations:** [mp3quran.net](https://mp3quran.net/eng/api)
- **Tafseer:** [api.quran-tafseer.co](http://api.quran-tafseer.com/en/docs/)
- **Hadith:** [hadithapi.com](https://hadithapi.com)

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/tilawah.git
   ```
2. **Open in Android Studio:** Use the latest version of Android Studio (Ladybug or newer).
3. **Build & Run:** Select your device/emulator and click the 'Run' button.

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to improve the app.

## 📄 License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
