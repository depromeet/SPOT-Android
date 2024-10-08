plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.depromeet.presentation"
    compileSdk = Constants.compileSdk

    defaultConfig {
        minSdk = Constants.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "MIXPANEL_TOKEN", getApiKey("dev.mixpanel_token"))
        }
        getByName("release") {
            buildConfigField("String", "MIXPANEL_TOKEN", getApiKey("prod.mixpanel_token"))
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmVersion
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

fun getApiKey(propertyKey: String): String {
    return com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))
    KotlinDependencies.run {
        implementation(kotlin)
        implementation(coroutines)
        implementation(jsonSerialization)
        implementation(dateTime)
    }

    implementation("com.google.accompanist:accompanist-permissions:0.28.0")
    AndroidXDependencies.run {
        implementation(coreKtx)
        implementation(appCompat)
        implementation(constraintLayout)
        implementation(fragment)
        implementation(startup)
        implementation(legacy)
        implementation(security)
        implementation(hilt)
        implementation(lifeCycleKtx)
        implementation(lifecycleJava8)
        implementation(splashScreen)
        implementation(pagingRuntime)
        implementation(workManager)
        implementation(hiltWorkManager)
    }

    KaptDependencies.run {
        kapt(hiltCompiler)
        kapt(hiltWorkManagerCompiler)
    }

    implementation(MaterialDesignDependencies.materialDesign)

    TestDependencies.run {
        testImplementation(jUnit)
        androidTestImplementation(androidTest)
        androidTestImplementation(espresso)
    }

    ThirdPartyDependencies.run {
        implementation(coil)
        implementation(coilSvg)
        implementation(platform(okHttpBom))
        implementation(okHttp)
        implementation(okHttpLoggingInterceptor)
        implementation(retrofit)
        implementation(retrofitJsonConverter)
        implementation(timber)
        implementation(ossLicense)
        implementation(progressView)
        implementation(balloon)
        implementation(lottie)
        implementation(lottieCompose)
        implementation(kakaoLogin)
        implementation(kakaoShare)
        implementation(indicator)
        implementation(shimmer)
        debugImplementation(flipperLeakCanary)
        debugImplementation(leakCanary)
        debugImplementation(soloader)
    }

    ComposeDependency.run {
        implementation(balloonCompose)
        implementation(cloudy)
    }

    Compose.forEach {
        implementation(it)
    }
    MixpanelDependencies.run {
        implementation(mixpanel)
    }
}

