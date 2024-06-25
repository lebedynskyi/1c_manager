plugins {
    kotlin("kapt")
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.application)

    alias(libs.plugins.jetbrains.kotlin.android)
    id(libs.plugins.jetbrains.parcelize.get().pluginId)

    kotlin("plugin.serialization") version "1.9.23"

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.simple.games.tradeassist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.simple.games.tradeassist"
        minSdk = 26
        targetSdk = 34
        versionCode = 20
        versionName = "0.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../tools/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            storeFile = file("../tools/release.keystore")
            keyAlias = "trade"
            keyPassword = "tradeKey"
            storePassword = "tradeKey"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            applicationIdSuffix  = ""
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix  = ".dev"
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) {
        it.packaging.resources.excludes.add("META-INF/**")
    }
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
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // To use Kotlin annotation processing tool (kapt)
    kapt(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room

    // _____________
    implementation(libs.coil)
    implementation(libs.retrofit)
    implementation(libs.retrofitLogger)
    implementation(libs.retrofitJsonSerializer)
    implementation(libs.kotlinSerialization)

    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)
    // _____________

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}