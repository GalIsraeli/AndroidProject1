AndroidProject1
===============

Overview:
---------
AndroidProject1 is a mobile game application that tracks player scores and displays a map-based leaderboard. It uses location services, Google Maps, and a simple gameplay mechanic with customizable speed and tilt modes.

Main Features:
--------------
- Game with configurable speed and tilt controls.
- Leaderboard with map integration showing where scores were achieved.
- Persistent data storage using shared preferences.
- Uses Glide for image loading and Google Maps SDK for displaying score locations.

Activities:
-----------
1. MainActivity:
   - Entry point of the app.
   - Allows players to enter their name and select gameplay options.
   - Navigation to the game or leaderboard screens.

2. GameActivity:
   - Core gameplay screen.
   - Handles the game loop and user interactions during play.
   - Captures player scores and location on completion.

3. LeaderboardActivity:
   - Displays top scores in a RecyclerView.
   - Shows locations of scores on an interactive Google Map.
   - Allows users to navigate back to MainActivity.

Core Libraries and Dependencies:
-------------------------------
- AndroidX (Core, AppCompat, ConstraintLayout)
- Google Play Services (Maps, Location)
- Glide (for image loading)
- Gson (for JSON parsing)
- Kotlin and View Binding
- RecyclerView for leaderboard display

Permissions:
------------
- Internet Access
- Vibration
- Coarse and Fine Location Access

Build Settings:
---------------
- Minimum SDK: 29
- Target SDK: 35
- Java Version: 11
- Kotlin JVM Target: 11

Package Name:
-------------
com.gamepackage.androidproject1

Custom Application Class:
-------------------------
- App.kt initializes shared preference manager (MSPV3) and signal handler (MySignal) at app startup.

---

To run the project:
1. Set your Google Maps API key in `secrets.properties`.
2. Sync Gradle and build the project.
3. Run on an Android device or emulator with Google Play services.
