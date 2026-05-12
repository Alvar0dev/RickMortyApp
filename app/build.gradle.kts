plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.rickmortyapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.rickmortyapp"
        minSdk = 33
        targetSdk = 36
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
}

dependencies {
    // ️ PICASSO: Descarga y caché de imágenes desde URL
    implementation("com.squareup.picasso:picasso:2.71828")
// ️ GOOGLE MAPS: Para mapas, marcadores y geolocalización (PMDM07)
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
// 🌐 Retrofit + GSON (Para descargar y leer el JSON de la API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}