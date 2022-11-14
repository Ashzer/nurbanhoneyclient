object Kotlin {
    const val standardLibrary = "1.7.10"
    const val coroutines = "1.3.9"
}

object AndroidSdk {
    const val min = 25
    const val compile = 33
    const val target = compile
}

object AndroidClient {
    const val appId = "org.devjj.platform.nurbanhoney"
    const val versionCode = 1
    const val versionName = "1.0"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "7.3.1"
        const val gradleVersion = "7.2"
        const val hilt = "2.44.1"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val androidHilt = "dagger.hilt.android.plugin"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val variants = "scripts.variants"
    const val quality = "scripts.quality"
    const val compilation = "scripts.compilation"
}

object Libraries {
    object Versions {
        const val hilt = BuildPlugins.Versions.hilt
        const val appCompat = "1.2.0"
        const val androidX = "1.0.0"
        const val constraintLayout = "2.0.4"
        const val recyclerView = "1.1.0"
        const val cardView = "1.0.0"
        const val material = "1.3.0"
        const val lifecycle = "2.2.0"
        const val lifecycleExtensions = "2.1.0"
        const val annotations = "1.1.0"
        const val ktx = "1.5.0"
        const val glide = "4.11.0"
        const val retrofit = "2.9.0"
        const val okHttpLoggingInterceptor = "4.9.0"
        const val playCore = "1.10.2"
        const val kakaoLogin = "2.8.1"
        const val naverLogin = "4.2.6"
        const val naverAndroidSDK = "4.2.6"
        const val googleLogin = "20.0.1"
        const val richEditor = "2.0.0"
        const val fragment_ktx = "1.2.5"
        const val image_cropper = "2.8.0"
        const val compose = "1.3.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Kotlin.coroutines}"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Kotlin.coroutines}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.androidX}"
    const val legacySupportCoreUtils =  "androidx.legacy:legacy-support-core-utils:${Versions.androidX}"
    const val browser = "androidx.browser:browser:${Versions.androidX}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.androidX}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val androidAnnotations = "androidx.annotation:annotation:${Versions.annotations}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val retrofit = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okHttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLoggingInterceptor}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
    const val kakaoLogin = "com.kakao.sdk:v2-user:${Versions.kakaoLogin}"
    const val naverLogin = "com.naver.nid:naveridlogin-android-sdk:${Versions.naverLogin}"
    const val naverAndroidSDK = "libs/naveridlogin_android_sdk_${Versions.naverAndroidSDK}.aar"
    const val googleLogin = "com.google.android.gms:play-services-auth:${Versions.googleLogin}"
    const val richEditor = "jp.wasabeef:richeditor-android:${Versions.richEditor}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"
    const val image_cropper = "com.theartofdev.edmodo:android-image-cropper:${Versions.image_cropper}"

    const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUITooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeUIToolingPreview ="androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeIconsCore = "androidx.compose.material:material-icons-core:${Versions.compose}"
    const val composeIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val composeLivedata = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeRxjava = "androidx.compose.runtime:runtime-rxjava2:${Versions.compose}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.13.1"
        const val mockk = "1.10.0"
        const val robolectric = "4.4"
        const val kluent = "1.14"
        const val testRunner = "1.4.0"
        const val espressoCore = "3.4.0"
        const val espressoIntents = "3.1.0"
        const val testExtensions = "1.1.3"
        const val testRules = "1.1.0"
        const val hiltTesting = BuildPlugins.Versions.hilt
        const val composeTest = Libraries.Versions.compose
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val espressoIntents =
        "androidx.test.espresso:espresso-intents:${Versions.espressoIntents}"
    const val testExtJunit = "androidx.test.ext:junit:${Versions.testExtensions}"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.hiltTesting}"
    const val composeUITest = "androidx.compose.ui:ui-test-junit4:${Versions.composeTest}"
}

object DevLibraries {
    private object Versions {
        const val leakCanary = "2.9.1"
    }

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
}
