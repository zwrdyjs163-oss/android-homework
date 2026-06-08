plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.tddscore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tddscore"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
