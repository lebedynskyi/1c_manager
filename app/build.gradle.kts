plugins {
    kotlin("kapt")
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.application)

    alias(libs.plugins.jetbrains.kotlin.android)
    id(libs.plugins.jetbrains.parcelize.get().pluginId)

    kotlin("plugin.serialization") version "1.9.23"
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
        versionCode = 13
        versionName = "0.1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // ---- Room
    val room_version = "2.6.1"

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