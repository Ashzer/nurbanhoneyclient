plugins {
    id(ScriptPlugins.infrastructure)
}

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath (BuildPlugins.androidGradlePlugin)
        classpath (BuildPlugins.kotlinGradlePlugin)
        classpath (BuildPlugins.hiltGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }

    }
}