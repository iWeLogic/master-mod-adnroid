import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

apply(from = "../scripts/git-versioner.gradle")

android {
    compileSdk = 34
    namespace = "com.iwelogic.minecraft.mods"

    defaultConfig {
        applicationId = "com.iwelogic.minecraft.mods"
        minSdk = 23
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = "1.0.$versionCode"
        project.archivesName =  "${applicationId}_$versionName"
    }

    signingConfigs {
        create("release") {
            storeFile = file("iWeLogic.jks")
            storePassword = "K4Apw36WM3"
            keyAlias = "Alias"
            keyPassword = "K4Apw36WM3"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
            manifestPlaceholders["statusCrashlytics"] = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isDebuggable = true
            manifestPlaceholders["statusCrashlytics"] = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //GoogleServices
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.android.play:core:1.10.3")
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.google.android.ump:user-messaging-platform:2.1.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    //DI Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Third
    implementation("com.github.amitshekhariitbhu:Fast-Android-Networking:1.0.2")
    implementation("com.github.skydoves:expandablelayout:1.0.7")
    implementation("org.zeroturnaround:zt-zip:1.14")
    implementation("com.github.Redman1037:TSnackBar:V2.0.0")
    implementation("com.facebook.android:facebook-android-sdk:13.0.0")
}

fun getVersionCode(): Int {
    return project.extra.get("gitVersion") as? Int ?: 1
}