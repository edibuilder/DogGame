# DogGame

An Android game app built with **Kotlin** and **Jetpack Compose**. Players create a profile, log in and compete on a local leaderboard. In the game, a dog catches falling treats and avoids bees.

> **Status:** work in progress. The account system, profile management and leaderboard are done; the gameplay loop is being finished (see [Roadmap](#roadmap)).

## Features

- **Registration and login** with validation (unique username, password of at least 8 characters with a lowercase letter, an uppercase letter and a digit)
- **Main menu** with a Dashboard and a Profile screen
- **Leaderboard** that ranks users by best score and shows their avatars
- **Profile management:** change username, change password, delete profile, log out
- **Random avatars** fetched from the [RoboHash](https://robohash.org) API (Robot, Monster, Robot Head, Kitten)
- **Local persistence** of users, scores and avatars with Room (SQLite)
- **Custom game engine** based on `SurfaceView`: touch-controlled dog, falling objects, difficulty that scales with score

## Tech stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Game rendering | `SurfaceView` + `Canvas` with a game thread |
| Database | Room (with KSP) |
| Networking | OkHttp, Kotlin Coroutines |
| Build | Gradle (Kotlin DSL), version catalog |

Min SDK 24, target SDK 35.

## Project structure

```
app/src/main/java/com/example/doggame/
├── datamanager/   Room database, DAO, entity, validation, logged-in user state
├── service/       Avatar API client
├── model/         Game objects (Dog, Biscuit, GiftBone, Bee)
├── game/          GameView (game loop, rendering, input)
└── *Activity.kt   Compose screens (login, register, menu, profile, dashboard...)
```

## Getting started

1. Install [Android Studio](https://developer.android.com/studio).
2. Clone the repository:
   ```
   git clone https://github.com/edibuilder/DogGame.git
   ```
3. Open the project folder in Android Studio and wait for the Gradle sync to finish.
4. Run the `app` configuration on an emulator or a physical device.

If Gradle reports an incompatible JVM, set **Settings → Build Tools → Gradle → Gradle JDK** to a JDK between 17 and 21.

An internet connection is needed for the avatar feature.

## Roadmap

- [ ] Start the game from the main menu
- [ ] Spawn falling objects, collision detection and scoring
- [ ] Game over flow and best-score saving
- [ ] Hash passwords instead of storing them in plain text
- [ ] Move database access off the main thread
- [ ] Align the KSP and Kotlin plugin versions
- [ ] Add screenshots

## Origin

The project started as a university lab exercise for a mobile application development course and is being extended as a personal project.
