plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sahanmw.roadsafetyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sahanmw.roadsafetyapp"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.google.firebase:firebase-storage:21.0.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.firebase:firebase-database:21.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.google.firebase:firebase-firestore:25.1.0")
    implementation ("com.google.firebase:firebase-database:21.0.0")


    // Firebase Authentication
    implementation ("com.google.firebase:firebase-auth:23.0.0")

    // Firestore
    implementation ("com.google.firebase:firebase-firestore:25.1.0")

    // Firebase UI (optional for better UI handling)
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

    implementation ("com.google.firebase:firebase-auth:23.0.0")


        implementation ("com.google.android.gms:play-services-auth:21.2.0")

        // Facebook SDK
        implementation ("com.facebook.android:facebook-android-sdk:16.0.1")


    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}