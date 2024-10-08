plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.starmi.star_calendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.starmi.star_calendar"
        minSdk = 26
        targetSdk = 34
        versionCode = 5
        versionName = "5.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.play.services.wearable)
    // Material Design components
    //noinspection UseTomlInstead
    implementation("com.google.android.material:material:1.12.0")
    //Gson
    //noinspection UseTomlInstead
    implementation ("com.google.code.gson:gson:2.10.1")

}