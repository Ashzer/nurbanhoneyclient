plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.androidHilt)

    //test
    id("de.mannodermaus.android-junit5")

    // Internal Script plugins
    id(ScriptPlugins.variants)
    id(ScriptPlugins.quality)
    id(ScriptPlugins.compilation)

    //coverage
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.5"
}



android {

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)

        applicationId = AndroidClient.appId
        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName
        testInstrumentationRunner = AndroidClient.testRunner

        testInstrumentationRunnerArgument(
            "runnerBuilder",
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
        )
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }

        //TODO: Remove this when migrating the DI framework
        getByName("main") { java.srcDir("$buildDir/generated/source/kapt/main") }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion = "1.3.1"
    }


    kotlinOptions {
        freeCompilerArgs = listOf("-Xallow-result-return-type")
    }

}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.3.5")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.0")

    //Compile time dependencies
    kapt(Libraries.lifecycleCompiler)
    kapt(Libraries.hiltCompiler)

    // Application dependencies
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.kotlinCoroutinesAndroid)
    implementation(Libraries.appCompat)
    implementation(Libraries.legacySupportCoreUtils)
    implementation(Libraries.browser)
    implementation(Libraries.legacySupport)
    implementation(Libraries.ktxCore)
    implementation(Libraries.constraintLayout)
    //implementation(Libraries.viewModel)
    implementation(Libraries.liveData)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.cardView)
    implementation(Libraries.recyclerView)
    implementation(Libraries.material)
    implementation(Libraries.androidAnnotations)
    implementation(Libraries.glide)
    implementation(Libraries.hilt)
    implementation(Libraries.retrofit)
    implementation(Libraries.okHttpLoggingInterceptor)
    implementation(Libraries.playCore)
    implementation(Libraries.kakaoLogin)
 //   implementation(Libraries.naverLogin)
    implementation(Libraries.googleLogin)
    //implementation(files(Libraries.naverAndroidSDK))
    // Dagger Core
    implementation("com.google.dagger:dagger:${BuildPlugins.Versions.hilt}")
    kapt("com.google.dagger:dagger-compiler:${BuildPlugins.Versions.hilt}")

    // Dagger Android
    api( "com.google.dagger:dagger-android:${BuildPlugins.Versions.hilt}")
    api( "com.google.dagger:dagger-android-support:${BuildPlugins.Versions.hilt}")
    kapt ("com.google.dagger:dagger-android-processor:${BuildPlugins.Versions.hilt}")


    implementation(Libraries.richEditor)
    implementation(Libraries.fragment_ktx)
    api(Libraries.image_cropper)

    implementation(Libraries.composeUI)
    // Tooling support (Previews, etc.)
    implementation(Libraries.composeUITooling)
    implementation(Libraries.composeUIToolingPreview)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(Libraries.composeFoundation)
    // Material Design
    implementation(Libraries.composeMaterial)
    // Material design icons
    implementation(Libraries.composeIconsCore)
    implementation(Libraries.composeIconsExtended)
    // Integration with observables
    implementation(Libraries.composeLivedata)
    implementation(Libraries.composeRxjava)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    // UI Tests
    androidTestImplementation(TestLibraries.composeUITest)

    // Unit/Android tests dependencies
    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.mockk)
    testImplementation(TestLibraries.kluent)
    testImplementation(TestLibraries.robolectric)

    // Acceptance tests dependencies
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espressoCore)
    androidTestImplementation(TestLibraries.testExtJunit)
    androidTestImplementation(TestLibraries.testRules)
    androidTestImplementation(TestLibraries.espressoIntents)
    androidTestImplementation(TestLibraries.hiltTesting)
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")

    // Junit5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.3.0")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.3.0")

    // Development dependencies
    debugImplementation(DevLibraries.leakCanary)

    debugImplementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")
}


