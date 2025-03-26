import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

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
    compileSdk = 35
    namespace = "com.iwelogic.minecraft.mods"

    defaultConfig {
        applicationId = "com.iwelogic.minecraft.mods"
        minSdk = 23
        targetSdk = 35
        versionCode = getVersionCode()
        versionName = "1.0.$versionCode"
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

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.6")

    //GoogleServices
    implementation("com.google.android.gms:play-services-ads:24.1.0")
    implementation("com.google.android.play:review-ktx:2.0.2")
    implementation("com.google.android.ump:user-messaging-platform:3.2.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
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
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    //Third
    implementation("com.github.amitshekhariitbhu.Fast-Android-Networking:android-networking:1.0.4")
    implementation("com.github.skydoves:expandablelayout:1.0.7")
    implementation("org.zeroturnaround:zt-zip:1.14")
    implementation("com.github.Redman1037:TSnackBar:V2.0.0")
}

fun getVersionCode(): Int {
    return project.extra.get("gitVersion") as? Int ?: 1
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                val fileName = output.outputFileName.orNull ?: ""
                val extension = fileName.substring(fileName.length -3)
                output.outputFileName = "Minecraft_${output.versionName}.$extension"
            }
        }
    }
}