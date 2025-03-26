plugins {
    id ("com.android.application") version "8.7.3" apply false
    id ("com.android.library")version "8.7.3" apply false
    id ("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id ("com.google.dagger.hilt.android") version "2.51.1" apply false
    id ("com.google.firebase.crashlytics") version "3.0.3" apply false
    id ("com.google.gms.google-services") version "4.4.2" apply false
    id ("androidx.navigation.safeargs.kotlin" )version "2.8.9" apply false
}

apply(from = "scripts/git-versioner.gradle")