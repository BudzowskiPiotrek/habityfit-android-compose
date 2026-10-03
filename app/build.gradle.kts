import java.util.Properties
import java.io.FileInputStream

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

android {
    namespace = "com.piodev.habityfit"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        applicationId = "com.example.setpoint"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "TOKEN_APP",
            "\"${localProperties.getProperty("TOKEN_APP", "")}\""
        )
        buildConfigField(
            "String",
            "LINK_APK",
            "\"${localProperties.getProperty("LINK_APK", "")}\""
        )
        buildConfigField(
            "String",
            "EMAIL_APP",
            "\"${localProperties.getProperty("EMAIL_APP", "")}\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // --- ICONOS DE MATERIAL DESIGN (Para usar Icons.Default.XXX) ---
    implementation("androidx.compose.material:material-icons-extended")
    // --- COIL: La librería para cargar imágenes ---
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-gif:2.7.0")

    // --- INTEGRACIÓN HILT Y COMPOSE (Indispensable para hiltViewModel()) ---
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // --- COMPOSE MATERIAL3 Y UI (Para que reconozca Column, Button, Text, etc.) ---
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // --- LIFECYCLE RUNTIME COMPOSE (Para collectAsStateWithLifecycle) ---
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

    // --- LA CONEXIÓN CLAVE ---
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))

    // --- HILT (Para que la App pueda usar la DB) ---
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.runtime.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.retrofit.core)

    // --- ViewModel KTX (Para usar viewModels en las actividades) ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    // --- RESTO DE TUS LIBRERÍAS ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}