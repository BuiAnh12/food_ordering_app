plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.food_ordering_mobile_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.food_ordering_mobile_app"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "35.0.0"
}

dependencies {
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment:2.8.9")
    implementation("androidx.navigation:navigation-ui:2.8.9")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("jp.wasabeef:glide-transformations:4.3.0")
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.gms:play-services-maps:19.1.0")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.12.0")
    implementation("com.google.code.gson:gson:2.12.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("io.socket:socket.io-client:2.0.0")
}