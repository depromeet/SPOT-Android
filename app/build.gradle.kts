plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

android {
    namespace = "com.example.depromeetandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.depromeetandroid"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    // ktx
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    // app compat
    implementation("androidx.appcompat:appcompat:1.7.0")

    // material
    implementation("com.google.android.material:material:1.12.0")

    // constraint layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // hilt
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hiltVersion"]}")
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")

    // lifecycle
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleVersion"]}")

    // recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutineVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutineVersion"]}")

    // network
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // coil
    implementation("io.coil-kt:coil:2.4.0")

    // serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // shared preference
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
}
