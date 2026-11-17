plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    kotlin("kapt")
}

android {
    namespace = "com.skuvahncaptain"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.skuvahncaptain"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "key", "\"b14ca5898a4e4133bbce2ea2315a1916\"")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "key", "\"b14ca5898a4e4133bbce2ea2315a1916\"")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    dataBinding {
        enable = true
    }
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)

    // Coroutines
    implementation(libs.coroutines.android)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    // Google Sign-In
    implementation(libs.google.auth)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.places)
    kapt(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room Database
    implementation(libs.room.db)
    implementation(libs.room.ktx.db)
    kapt(libs.room.db.compiler)

    //Shared Preferences
    implementation(libs.security.crypto)

    //Root devices check
    implementation(libs.scottyab)

    //pull to refresh
    implementation(libs.swiperefreshlayout)

    //Glide
    implementation(libs.glide)

    //Font management
    implementation(libs.sdp)
    implementation(libs.ssp)

    //OTP View
    implementation(libs.otpview)

    //Work Manager
    implementation(libs.workmanager)

    //Circle ImageView
    implementation(libs.circleimageview)

    //Work Manager
    implementation(libs.firebase.storage)

    //Work Manager
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.media)

    implementation(libs.s3)
    implementation(libs.s3.core)

}