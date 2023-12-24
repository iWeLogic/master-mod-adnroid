plugins {
    id ("com.android.application") version "8.2.0" apply false
    id ("com.android.library")version "8.2.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
    id ("com.google.firebase.crashlytics") version "2.9.6" apply false
    id ("com.google.gms.google-services") version "4.3.15" apply false
    id ("androidx.navigation.safeargs" )version "2.5.1" apply false
}


apply(from = "scripts/git-versioner.gradle")