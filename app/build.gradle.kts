plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")

}



android {
    namespace = "com.example.mytest2024"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mytest2024"
        minSdk = 29
        targetSdk = 34
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
        viewBinding = true
        dataBinding = true

    }



}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1'")
    implementation("androidx.activity:activity:1.8.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("com.google.android.material:material:1.9.0")

    /* Okhttp, Retrofit */
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")


    /*Gson*/
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.code.gson:gson:2.8.6")

    /*Coil*/
    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-gif:2.2.2") // Coil에서 GIF를 지원하는 라이브러리

    /*Glide*/
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")


    /* bottomNavigation */
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    /*CardView*/
    implementation("androidx.cardview:cardview:1.0.0")


    /*Coroutine*/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    /* viewModel */
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    /* LiveData */
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    /* 생체 인증 */
    implementation ("androidx.biometric:biometric:1.1.0")

    /* LifeCycle 감지 */
    implementation ("androidx.lifecycle:lifecycle-process:2.5.1")


    /* RxJava */
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.6")
    implementation ("io.reactivex.rxjava3:rxkotlin:3.0.1")

    /* Rx binding*/
    implementation ("com.jakewharton.rxbinding4:rxbinding:4.0.0")



    /*Firebase*/

    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging:24.1.0")

}
