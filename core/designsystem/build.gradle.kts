plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.depromeet.designsystem"
    compileSdk = Constants.compileSdk

    defaultConfig {
        minSdk = Constants.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
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
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmVersion
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
}

dependencies {
    AndroidXDependencies.run {
        implementation(coreKtx)
        implementation(appCompat)
    }

    implementation(MaterialDesignDependencies.materialDesign)

    TestDependencies.run {
        testImplementation(jUnit)
        androidTestImplementation(androidTest)
    }

    ComposeDependency.run {
        implementation(material)
        implementation(ui)
    }
}