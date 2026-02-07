import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.amrk000.repairit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.amrk000.repairit"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //build config api key
        val properties = Properties()
        properties.load(File("local.properties").inputStream())

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            properties.getProperty("GEMINI_API_KEY")
        )

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    viewBinding{
        enable = true
    }

    androidResources {
        generateLocaleConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Compose Constraint Layout
    implementation (libs.androidx.constraintlayout.compose)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // ViewModel:
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0") //for Compose
    // Saved state module:
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.10.0")

    //Camera X
    val camerax_version = "1.5.3"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:$camerax_version")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:$camerax_version")

    // ML Kit:
    //Text Recognition:
    implementation ("com.google.mlkit:text-recognition:16.0.1")

    //Splash
    implementation ("androidx.core:core-splashscreen:1.0.1")

    //Room DB
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    //Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    //Gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    //Gson
    implementation("com.google.code.gson:gson:2.13.2")

}