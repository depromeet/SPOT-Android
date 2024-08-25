plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = Versions.javaVersion
    targetCompatibility = Versions.javaVersion
}

dependencies {
    JavaDependencies.run {
        implementation(javaxInject)
    }
    KotlinDependencies.run {
        implementation(kotlin)
        implementation(coroutines)
        implementation(dateTime)
    }
    AndroidXDependencies.run {
        implementation(pagingCommon)
    }
}
