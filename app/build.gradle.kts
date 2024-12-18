plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.soundmood"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.soundmood"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Tambahkan BuildConfig untuk Spotify Client ID
        buildConfigField ("String", "SPOTIFY_CLIENT_ID", "\"b9d3146baa9a46b49bf9ddeb22aac967\"")
        buildConfigField ("String", "SPOTIFY_REDIRECT_URI", "\"com.example.authorizationtest://callback\"")
    }

    buildTypes {
        debug {
            // Aktifkan BuildConfig pada build type debug
            buildConfigField ("String", "SPOTIFY_CLIENT_ID", "\"b9d3146baa9a46b49bf9ddeb22aac967\"")
            buildConfigField ("String", "SPOTIFY_REDIRECT_URI", "\"com.example.authorizationtest://callback\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Aktifkan BuildConfig pada build type release (jika berbeda nilai dapat diatur di sini)
            buildConfigField ("String", "SPOTIFY_CLIENT_ID", "\"b9d3146baa9a46b49bf9ddeb22aac967\"")
            buildConfigField ("String", "SPOTIFY_REDIRECT_URI", "\"com.example.authorizationtest://callback\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true // Aktifkan fitur BuildConfig
    }
}

dependencies {

    // Android x Material
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // constrain & coordinator layout
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)

    // retrofit,gson & okhttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp.v4110)
    implementation(libs.logging.interceptor)

    // spotify
    implementation(files("spotify-app-remote-release-0.8.0.aar"))
    implementation(libs.auth)

    // navigation
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)

    // Datastore, ViewModel & LiveData
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Glide
    implementation(libs.glide)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    // TensorFlowLite
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
}



