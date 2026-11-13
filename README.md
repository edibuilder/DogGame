# DogGame

An Android game app built with **Kotlin** and **Jetpack Compose**. Players create a profile, log in and compete on a local leaderboard. In the game, a dog catches falling treats and avoids bees.

## Gameplay

Drag your finger left or right to move the dog.

| Object | Effect |
|--------|--------|
| Biscuit | +5 points |
| Gift bone | +25 points. Missing one counts as a miss |
| Bee | −20 points (score never drops below zero) |

The game ends after **5 missed gift bones**. Objects fall more often as the score grows past 200, 500 and 1000 points. The best score is saved to the player's profile and shown on the leaderboard.

## Features

- **Registration and login** with validation (unique username, password of at least 8 characters with a lowercase letter, an uppercase letter and a digit)
- **Leaderboard** that ranks users by best score and shows their avatars
- **Profile management:** change username, change password, delete profile, log out
- **Random avatars** fetched from the [RoboHash](https://robohash.org) API (Robot, Monster, Robot Head, Kitten)
- **Local persistence** of users, scores and avatars with Room (SQLite)
- **Custom game engine** with a fixed-step game loop drawn on a `Canvas`

## Architecture

The game is split into layers so the rules can be tested without a device:

| Layer | Responsibility |
|-------|----------------|
| `model` | Plain Kotlin game objects: `Dog`, `FallingObject`, `Hitbox` |
| `GameEngine` | Game state and rules: spawning, falling, collisions, scoring, difficulty |
| `GameConfig` / `GameLayout` | Tunable values and screen-relative sizes |
| `GameAssets` | Loads and scales sprites once |
| `GameRenderer` | Draws the scene and the HUD on a `Canvas` |
| `GameView` | Frame loop on `Choreographer` with fixed-step updates, touch input, pause when hidden; reports game over through a callback |

The rest of the app uses Jetpack Compose screens backed by a Room database.

## Tech stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Game rendering | Custom `View` + `Canvas` |
| Database | Room (with KSP) |
| Networking | OkHttp, Kotlin Coroutines |
| Testing | JUnit 4 |
| Build | Gradle (Kotlin DSL), version catalog |

Min SDK 24, target SDK 35.

## Project structure

```
app/src/main/java/com/example/doggame/
├── datamanager/   Room database, DAO, entity, validation, logged-in user state
├── service/       Avatar API client
├── model/         Game objects and hitboxes
├── game/          Engine, config, assets, renderer, loop and view
└── *Activity.kt   Screens (login, register, menu, game, profile, dashboard...)
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

## Running tests

```
./gradlew test
```

## Roadmap

- [x] Start the game from the main menu
- [x] Falling objects, collision detection and scoring
- [x] Game over flow and best-score saving
- [ ] Hash passwords instead of storing them in plain text
- [ ] Move database access off the main thread
- [ ] Add screenshots

## Origin

The project started as a university lab exercise for a mobile application development course and is being extended as a personal project.
