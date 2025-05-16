# Dodge Rush

An Android arcade game built with Kotlin where players race to dodge incoming enemies, collect coins, and climb the leaderboard. The game features dynamic controls, location-aware score tracking, and a sleek leaderboard interface.

## 📦 Overview

Dodge Rush challenges players to survive as long as possible by dodging enemies that descend from the top of the screen. Players can move left and right using on-screen buttons, or by tilting their phone when **Tilt Mode** is enabled. Collecting coins increases the player's score, and the top 10 scores are stored locally using `SharedPreferences`.

## 🎮 Features

- **Speed Mode**: Toggle the difficulty by increasing enemy speed.
- **Tilt Mode**: Replace on-screen controls with phone tilt-based movement.
- **Leaderboard Screen**: Displays the top 10 local scores.
  - Tap on a score to zoom the map below to the exact location where the score was recorded.
  - Top 3 scores are visually highlighted in gold, silver, and bronze.
- **MapView Integration**: Google Maps displays score locations.
- **Local Data Storage**: High scores saved persistently via `SharedPreferences`.
- **Modern UI**: Material Design components with custom backgrounds for a clean, engaging interface.

## 📱 Screens

- **Main Menu**
  - Start Game
  - Speed Mode toggle
  - Tilt Mode toggle
  - Leaderboard button

- **Game Screen**
  - Enemies fall from the top of the screen.
  - Move left/right via buttons or device tilt.
  - Collect coins to increase score.

- **Leaderboard Screen**
  - RecyclerView listing top 10 players.
  - MapView zooms to score locations.
  - Top 3 scores styled distinctly.

## 🛠️ Tech Stack

- **Language**: Kotlin
- **IDE**: Android Studio
- **UI**: Material Design 3, RecyclerView, MapView (Google Maps)
- **Data Storage**: `SharedPreferences`
- **Minimum SDK**: `API 21+`
- **Target SDK**: `API 34`

## 📦 Dependencies

- `com.google.android.material:material`
- `com.google.android.gms:play-services-maps`
- `androidx.recyclerview:recyclerview`
- `androidx.appcompat:appcompat`

## 📍 Future Improvements

- Implement global leaderboards with a backend.
- Add power-ups and new enemy types.
- Integrate sound effects and background music.
- Customizable player avatars.

## 📖 How to Run

1. Clone this repository.
2. Open in Android Studio.
3. Sync Gradle.
4. Replace your Google Maps API key in `AndroidManifest.xml`.
5. Run the app on a physical device or emulator (tilt control requires an actual device).

---

## 📌 License

This project is for educational and personal use.

