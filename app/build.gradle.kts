plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.msa.composecraft"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.msa.composecraft"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //material.icons
    implementation(libs.androidx.material.icons.extended.android)

    //coil loading image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //pager image
    implementation (libs.accompanist.pager)
    implementation (libs.accompanist.pager.indicators)

    // lottie
    implementation (libs.lottie.compose)
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.36.0")

    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //SMSRetrieval API Dependencies for Auto OTP Verification
    implementation("com.google.android.gms:play-services-auth-api-phone:18.2.0")
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    implementation("androidx.media3:media3-exoplayer:1.7.1")
    implementation("androidx.media3:media3-ui:1.7.1")

    implementation ("androidx.media3:media3-exoplayer:1.7.1")
    implementation ("androidx.media3:media3-ui:1.7.1")
    implementation ("androidx.compose.foundation:foundation:1.8.2")
    implementation ("androidx.compose.material3:material3:1.3.2")

    //androidx.navigation
    implementation(libs.androidx.navigation.compose)
}