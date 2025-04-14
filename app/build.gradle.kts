plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
//    id("org.web3j") version "4.5.0"
    //
}

android {
    namespace = "com.hexagraph.cropchain"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hexagraph.cropchain"
        minSdk = 24
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
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.runtime.ktx)

    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose navigation:
    implementation(libs.androidx.navigation.compose)
    implementation(kotlin("script-runtime"))
    implementation("androidx.navigation:navigation-compose:2.8.9")



    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.google.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // serializable
    implementation(libs.kotlinx.serialization.json)

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

//    implementation ("com.github.bumptech.glide:glide:4.16.0")
//    implementation ("com.google.accompanist:accompanist-glide:0.30.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    //Web3j
    implementation(libs.web3j.core)
    implementation(libs.web3j.crypto)
    implementation(libs.web3j.utils)

    implementation("androidx.compose.material:material-icons-extended:1.7.6")

    //MetaMask
    implementation(libs.metamask.android.sdk)

    implementation("androidx.work:work-runtime-ktx:2.10.0")

    implementation("androidx.hilt:hilt-work:1.2.0")

    //DataStore dependency
    implementation("androidx.datastore:datastore-preferences:1.1.4")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")


}