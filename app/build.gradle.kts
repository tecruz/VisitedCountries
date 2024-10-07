/*
 * Designed and developed by 2024 tecruz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.example.visitedcountries.compose.Configuration
import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.example.visitedcountries.android.application")
  id("com.example.visitedcountries.android.application.compose")
  id("com.example.visitedcountries.android.hilt")
  id("com.example.visitedcountries.spotless")
  id("com.example.visitedcountries.detekt")
}

android {
  namespace = "com.example.visitedcountries"

  defaultConfig {
    applicationId = "com.example.visitedcountries"
    versionCode = Configuration.VERSION_CODE
    versionName = Configuration.VERSION_NAME
    testInstrumentationRunner = "com.example.visitedcountries.test.AppTestRunner"
  }

  signingConfigs {
    val properties = Properties()
    val localPropertyFile = project.rootProject.file("local.properties")
    if (localPropertyFile.canRead()) {
      properties.load(FileInputStream("$rootDir/local.properties"))
    }
    create("release") {
      storeFile = file(properties["RELEASE_KEYSTORE_PATH"] ?: "../keystores/visitedcountries.jks")
      keyAlias = properties["RELEASE_KEY_ALIAS"].toString()
      keyPassword = properties["RELEASE_KEY_PASSWORD"].toString()
      storePassword = properties["RELEASE_KEYSTORE_PASSWORD"].toString()
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles("proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")

      kotlinOptions {
        freeCompilerArgs += listOf(
          "-Xno-param-assertions",
          "-Xno-call-assertions",
          "-Xno-receiver-assertions"
        )
      }

      packaging {
        resources {
          excludes += listOf(
            "DebugProbesKt.bin",
            "kotlin-tooling-metadata.json",
            "kotlin/**",
            "META-INF/*.version"
          )
        }
      }
    }
  }

  buildFeatures {
    buildConfig = true
  }

  hilt {
    enableAggregatingTask = true
  }

  kotlin {
    sourceSets.configureEach {
      kotlin.srcDir(layout.buildDirectory.files("generated/ksp/$name/kotlin/"))
    }
    sourceSets.all {
      languageSettings {
        languageVersion = "2.0"
      }
    }
  }

  testOptions.unitTests {
    isIncludeAndroidResources = true
    isReturnDefaultValues = true
  }
}

dependencies {
  // features
  implementation(projects.feature.home)
  implementation(projects.feature.details)

  // cores
  implementation(projects.core.navigation)
  implementation(projects.core.designsystem)
  implementation(projects.core.test)

  //test
  testImplementation(libs.androidx.navigation.testing)
  testImplementation(libs.robolectric)

  //kover
  kover(projects.core.network)
  kover(projects.core.data)
  kover(projects.core.database)
  kover(projects.feature.home)
  kover(projects.feature.details)
}