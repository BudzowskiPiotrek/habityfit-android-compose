# HabityFit Android

Native Android client for **HabityFit**, an offline-first strength training platform.
Users log workouts without connectivity; changes are stored locally and synchronized later with the [HabityFit API](https://github.com/BudzowskiPiotrek/habityfit-api-dotnet).

> Private repository. Backend is public, this client is kept private for commercial reasons.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-3DDC84?style=flat-square&logo=android&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-DI-orange?style=flat-square)
![Retrofit](https://img.shields.io/badge/Retrofit-Moshi-green?style=flat-square)

---

## Screenshots

| Routines | Workout session | Progress | Report |
|---|---|---|---|
| ![](docs/routines.png) | ![](docs/workout.png) | ![](docs/progress.png) | ![](docs/report.png) |

---

## Features

| Area | Description |
|---|---|
| Routines | Create, edit and share routines with friends |
| Workout tracking | Sessions, sets, reps, weight, RPE and rest timer |
| Exercise library | Searchable catalog with GIF demos and an interactive body map (SVG) to filter by muscle |
| Progress | Custom Canvas charts: estimated 1RM, volume and max weight per session |
| Report | Weekly / monthly / yearly stats and sets per muscle group vs. recommended range |
| 1RM calculator | Brzycki formula |
| Body composition | Weight, muscle, fat, water; derived fat % and BMR |
| Social | Friend requests, email invitations, routine requests, activity feed |

---

## Architecture

```text
:app            UI (Compose), navigation, ViewModels, feature domain
:core:data      Sync repository and mappers
:core:database  Room entities, DAOs, migrations
:core:network   Retrofit, Moshi, OkHttp, session / JWT management
```

- Clean architecture per feature: `data` / `domain` / `presentation`
- MVVM with `StateFlow` and Compose state
- Dependency injection with Hilt
- Room as single source of truth, UI observes `Flow`

---

## Offline-first Synchronization

```text
UI → Room (SyncStatus = PENDING) → Push → API
UI ← Room (SyncStatus = SYNCED)  ← Pull ← API (delta since LAST_SYNC)
```

- Every entity carries `CREATED_AT`, `UPDATED_AT`, `DELETED_AT` and `SYNC_STATUS`
- Soft delete so deletions propagate across devices
- Push sends only `PENDING` rows and marks as `SYNCED` the ones the server confirms
- Pull requests only entities changed since the last sync anchor
- JWT stored locally and refreshed during sync

---

## Tech Stack

Kotlin 2.0 • Jetpack Compose • Material 3 • Navigation Compose • Room • Hilt (KSP) • Retrofit • Moshi • OkHttp • Coil (GIF) • Coroutines / Flow

---

## Build

Requires `local.properties` (not versioned):

```properties
BASE_URL=<api-base-url>
TOKEN_APP=<app-token>
```
