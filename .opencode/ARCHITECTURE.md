# Arquitectura del Proyecto

Documentación basada en el código y la configuración verificados del repositorio. Lo no verificable se marca como `Desconocido`.

## 1. Stack Tecnológico

- **Lenguaje:** Kotlin 2.0.20 (`gradle/libs.versions.toml`).
- **Plataforma:** Android nativo.
- **Build:** Gradle con Kotlin DSL y version catalog. AGP 8.12.0. Wrapper en `gradle/wrapper/`.
- **SDK:** `minSdk = 24`, `targetSdk = 35`; `compileSdk` 35 en `:app` y 34 en los módulos `:core:*`. Java/Kotlin JVM target 17.
- **UI:** Jetpack Compose, Material 3 1.2.1, Navigation Compose 2.8.0, Material Icons Extended.
- **DI:** Hilt 2.51.1 con KSP.
- **Persistencia:** Room 2.6.1 (runtime, ktx, compiler vía KSP).
- **Red:** Retrofit 2.11.0 (también `converter-moshi` 2.9.0 declarado en `:core:network`), OkHttp 4.12.0, Moshi 1.15.0 y adaptadores Moshi.
- **Seguridad local:** `androidx.security:security-crypto` 1.1.0 (`EncryptedSharedPreferences`).
- **Imágenes/GIF:** Coil Compose y Coil GIF 2.7.0.
- **Asincronía:** Coroutines y Flow.
- **Testing:** JUnit 4.13.2, AndroidX JUnit 1.2.1, Espresso 3.6.1 (dependencias declaradas; no se identificaron tests en el árbol fuente).

## 2. Árbol de Directorios

```text
.
├── app/                      Módulo Android de aplicación: UI Compose, ViewModels, features, DI y recursos.
│   └── src/main/
│       ├── java/com/piodev/habityfit/
│       │   ├── core/         Componentes UI, configuración, DI, modelos, tema y utilidades.
│       │   ├── features/     Features con capas data/domain/presentation.
│       │   └── presentation/ MainActivity y MainViewModel (arranque y navegación raíz).
│       ├── assets/           GIFs de ejercicios, imágenes y SVG del cuerpo.
│       └── res/              Recursos Android.
├── core/
│   ├── database/             Room: AppDatabase, entidades, DAOs, conversores, migraciones y DI.
│   ├── network/              Retrofit/Moshi/OkHttp, DTOs, sesión JWT y DI de red.
│   └── data/                 Repositorio de sincronización y mappers DTO ↔ entidad.
├── gradle/libs.versions.toml Versiones y alias de dependencias/plugins.
├── build.gradle.kts          Plugins raíz.
├── settings.gradle.kts       Módulos incluidos y repositorios.
├── gradle.properties         Propiedades de Gradle (contenido no documentado por política de secretos).
└── local.properties          Configuración local no versionada; aporta credenciales/URLs a BuildConfig.
```

Carpetas excluidas de la documentación por ser generadas o configuración local: `build/`, `app/build/`, `core/*/build/`, `.gradle/`, `.kotlin/`, `.idea/`.

## 3. Arquitectura General

Aplicación Android **offline-first** organizada en módulos por capas:

- `:app` concentra la capa de presentación y la lógica por feature. Cada feature sigue, con distinto grado de completitud, la estructura `data` (repositorios y mappers), `domain` (modelos y contratos de repositorio) y `presentation` (estado, UI Compose y ViewModel).
- `:core:database` es la fuente de verdad local (Room).
- `:core:network` encapsula la API remota (Retrofit) y la sesión JWT.
- `:core:data` implementa la sincronización entre red y base de datos y los mappers de transporte.

Patrón **MVVM** con Hilt para inyección de dependencias. La UI observa estado de ViewModels/Flows y Room.

Features identificadas en `app/src/main/java/com/piodev/habityfit/features/`: `account`, `app`, `auth`, `community`, `exercises`, `feed`, `graphics`, `informe`, `measurements`, `rm`, `routine`, `workout`.

## 4. Punto de Entrada

- **Aplicación:** `com.piodev.habityfit.HabityFitApp` (`@HiltAndroidApp`), declarada en `app/src/main/AndroidManifest.xml`.
- **Activity de entrada:** `com.piodev.habityfit.presentation.MainActivity` (`@AndroidEntryPoint`, launcher y deep link `habityfit://activate`). Procesa el parámetro `token` del deep link y lo entrega a `MainViewModel`.
- **Inicialización:**
  1. Hilt construye el grafo (`DatabaseModule`, `NetworkModule`, `RepositoryModule`).
  2. `MainActivity` crea `MainViewModel`, aplica `SetpointTheme` y renderiza la pantalla según `MainViewModel.currentScreen`.
  3. `MainViewModel.init` llama a `checkSession()`: valida expiración (`LimitsConfig`), token de `SessionManager`, aceptación de términos y decide entre `Loading`, `Login`, `Register`, `Terms`, `Expired` o `Home`.
  4. Con sesión válida e internet, ejecuta `SyncRepository.syncAll()`.

## 5. Flujo de Datos

```text
UI Compose → ViewModel → Repository (feature) → Room (estado local, SYNC_STATUS)
                                   │
                                   └── SyncRepository
                                         push: PENDING → API (/api/sync/push)
                                         pull: API (/api/sync/pull) → Room
```

- **Escrituras locales:** las entidades incluyen metadatos de sincronización (`CREATED_AT`, `UPDATED_AT`, `DELETED_AT`, `SYNC_STATUS`) y se marcan como pendientes.
- **Push:** `SyncRepositoryImpl.pushLocalChanges()` recopila filas pendientes de todos los DAOs, las empaqueta en `SyncPayloadDto`, llama a `FitnessApiService.uploadSyncData()` y marca como sincronizadas las confirmadas en la respuesta.
- **Pull:** `pullRemoteChanges()` obtiene el usuario y su `lastSyncDate`, llama a `downloadSyncData()`, guarda el token renovado si existe, inserta/actualiza entidades y actualiza el ancla temporal.
- **Orden:** `syncAll()` ejecuta push y después pull; devuelve `Result` y los errores se capturan en el llamador.
- **Lecturas UI:** repositorios de cada feature consultan DAOs y exponen `Flow` hacia los ViewModels.

## 6. Gestión del Estado

- **Estado persistente principal:** Room, base de datos `setpoint_db`, versión 4, con migraciones 1→2, 2→3 y 3→4 en `DatabaseModule`. `AppDatabase` agrupa 14 entidades y 16 DAOs (incluido `InformeDao`).
- **Sesión:** `SessionManager` usa `EncryptedSharedPreferences` (`setpoint_auth_prefs_encrypted`) para JWT e ID de usuario, con migración desde las preferencias legacy y validación de expiración del JWT.
- **Estado de UI:** `mutableStateOf` en `MainViewModel` y `StateFlow`/estados de presentación por feature; Compose consume el estado.
- **Navegación:** `navigation-compose` dentro de `AppScreen`, con rutas `routines`, `exercises`, `workouts`, `feed`, `profile` y subrutas de detalle/edición.
- **Sin gestor de estado global identificado** (por ejemplo, Redux/MVI framework): el estado se distribuye entre ViewModels y repositorios.

## 7. Archivos Críticos

| Archivo | Responsabilidad | Dependencias relevantes |
|---|---|---|
| `app/src/main/AndroidManifest.xml` | Declaración de app, activity, permisos y deep link. | `HabityFitApp`, `MainActivity`, recursos XML. |
| `app/src/main/java/com/piodev/habityfit/presentation/MainActivity.kt` | Entrada Compose y enrutado raíz. | `MainViewModel`, pantallas de login/registro/app/términos/expirado. |
| `app/src/main/java/com/piodev/habityfit/presentation/MainViewModel.kt` | Sesión, expiración, términos y sync inicial. | `SessionManager`, `NetworkHelper`, `SyncRepository`, `AuthRepository`, `LimitsConfig`. |
| `app/src/main/java/com/piodev/habityfit/features/app/AppScreen.kt` | Scaffold y navegación inferior. | Navigation Compose, `AppMenuScreen`, `NavOption`. |
| `app/src/main/java/com/piodev/habityfit/core/di/RepositoryModule.kt` | Enlace de interfaces de repositorio a implementaciones. | Implementaciones de todas las features y `SyncRepositoryImpl`. |
| `core/database/src/main/java/com/piodev/database/AppDatabase.kt` | Definición de Room y DAOs. | Entidades, DAOs, `Converters`. |
| `core/database/src/main/java/com/piodev/database/di/DatabaseModule.kt` | Construcción de BD, migraciones y provisión de DAOs. | Room, `AppDatabase`. |
| `core/network/src/main/java/com/piodev/network/dto/di/NetworkModule.kt` | Moshi, OkHttp, Retrofit y `FitnessApiService`. | `BuildConfig.BASE_URL`, `AuthInterceptor`, adaptadores Moshi. |
| `core/network/src/main/java/com/piodev/network/dto/common/SessionManager.kt` | Persistencia cifrada y validación del JWT. | `EncryptedSharedPreferences`, `MasterKey`. |
| `core/network/src/main/java/com/piodev/network/dto/api/FitnessApiService.kt` | Endpoints REST de sync, login e invitaciones. | Retrofit, DTOs. |
| `core/data/src/main/java/com/piodev/data/repository/SyncRepositoryImpl.kt` | Push/pull y mapeo de datos. | Todos los DAOs, `FitnessApiService`, `SessionManager`, mappers. |
| `app/build.gradle.kts` | Configuración Android, BuildConfig y dependencias de app. | Version catalog, `local.properties`, Compose, Hilt, Coil, módulos core. |
| `gradle/libs.versions.toml` | Catálogo de versiones y plugins. | Build Gradle completo. |
| `settings.gradle.kts` | Módulos y repositorios. | `:app`, `:core:database`, `:core:network`, `:core:data`. |

## Observaciones Verificadas

- El `applicationId` es `com.example.setpoint`, mientras el namespace de código es `com.piodev.habityfit`.
- `:core:network` declara Retrofit 2.11.0 vía catálogo y además `converter-moshi` 2.9.0 directamente; conviene tratarlo como deuda técnica.
- `:app` usa `compileSdk = 35` y `:core:data` usa `compileSdk = 34` (literal), frente a `compileSdk = release(34)` en los otros módulos core.
- No se identificaron directorios de tests (`src/test`, `src/androidTest`) en el código fuente, aunque hay dependencias de testing declaradas.
