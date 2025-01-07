plugins {
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")

    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    id(libs.plugins.jetbrains.parcelize.get().pluginId)

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.room")
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.simple.games.tradeassist"
    compileSdk = 35

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.simple.games.tradeassist"
        minSdk = 26
        targetSdk = 35
        versionCode = 27
        versionName = "0.2.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
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

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlinOptions {
        jvmTarget = "17"
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
    ksp(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room

    // _____________
    implementation(libs.coil)
    implementation(libs.retrofit)
    implementation(libs.retrofitLogger)
    implementation(libs.retrofitJsonSerializer)
    implementation(libs.kotlinSerialization)

    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    ksp(libs.hiltCompiler)
    // _____________

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}