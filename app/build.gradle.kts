plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.androidHilt)

    // Internal Script plugins
    id(ScriptPlugins.variants)
    id(ScriptPlugins.quality)
    id(ScriptPlugins.compilation)
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
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }

        //TODO: Remove this when migrating the DI framework
        getByName("main") { java.srcDir("$buildDir/generated/source/kapt/main") }
    }
    buildFeatures {
        viewBinding = true
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
    implementation(Libraries.viewModel)
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
    implementation(Libraries.naverLogin)
    implementation(Libraries.googleLogin)
    //implementation(files(Libraries.naverAndroidSDK))

    implementation(Libraries.richEditor)
    implementation (Libraries.fragment_ktx)
    api(Libraries.image_cropper)

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

    // Development dependencies
    debugImplementation(DevLibraries.leakCanary)
}


