plugins {
    id(ScriptPlugins.infrastructure)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
//        classpath (BuildPlugins.androidGradlePlugin)
//        classpath (BuildPlugins.kotlinGradlePlugin)
        classpath (BuildPlugins.hiltGradlePlugin)
        classpath ("de.mannodermaus.gradle.plugins:android-junit5:1.8.0.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}


