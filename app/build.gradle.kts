import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlinVersion
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = Constants.packageName
    compileSdk = Constants.compileSdk

    defaultConfig {
        applicationId = Constants.packageName
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk
        versionCode = Constants.versionCode
        versionName = Constants.versionName
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${getApiKey("kakaoApiKey")}\"")
        buildConfigField("String", "SVG_BASE_URL", getApiKey("svg.base.url"))
        manifestPlaceholders["kakaoApiKey"] = getApiKey("kakaoApiKey")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release-spot-key.jks")
            storePassword = getApiKey("store.key.password")
            keyAlias = getApiKey("key.alias")
            keyPassword = getApiKey("key.password")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {

        create("dev") {

            buildConfigField("String", "BASE_URL", getApiKey("dev.base.url"))
            buildConfigField("String", "S3_URL", getApiKey("dev.s3.base.url"))
            dimension = "version"
        }
        create("prod") {
            buildConfigField("String", "BASE_URL", getApiKey("prod.base.url"))
            buildConfigField("String", "S3_URL", getApiKey("prod.s3.base.url"))
            dimension = "version"
        }
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
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    KotlinDependencies.run {
        implementation(kotlin)
        implementation(coroutines)
        implementation(jsonSerialization)
        implementation(dateTime)
    }

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
        implementation(kakaoLogin)
        debugImplementation(flipperLeakCanary)
        debugImplementation(leakCanary)
        debugImplementation(soloader)
    }

    FirebaseDependencies.run {
        implementation(platform(bom))
        implementation(analytics)
        implementation(crashlytics)
    }
}
