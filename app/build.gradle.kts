import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlinVersion
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val kakaoApiKey = properties["kakaoApiKey"].toString()

android {
    namespace = Constants.packageName
    compileSdk = Constants.compileSdk

    defaultConfig {
        applicationId = Constants.packageName
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk
        versionCode = Constants.versionCode
        versionName = Constants.versionName

//        val properties = Properties()
//        properties.load(FileInputStream(rootProject.file("local.properties")))

        buildConfigField("String", "BASE_URL", getApiKey("base.url"))
        buildConfigField("String", "SVG_BASE_URL", getApiKey("svg.base.url"))
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("kakao.api.native.key"))
//        val kakaoApiKey = properties.getProperty("kakaoApiKey")
        manifestPlaceholders["kakaoApiKey"] = getApiKey("kakao.api.native.key")
//        manifestPlaceholders["kakaoApiKey"] = kakaoApiKey
//        manifestPlaceholders["kakaoApiKey"] = properties["kakao.api.native.key"] as String
//        addManifestPlaceholders(mapOf("kakaoApiKey" to getApiKey("kakao.api.native.key")))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            manifestPlaceholders["kakaoApiKey"] = kakaoApiKey
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
}
