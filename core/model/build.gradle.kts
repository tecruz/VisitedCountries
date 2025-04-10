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

plugins {
  id("com.example.visitedcountries.android.library")
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.visitedcountries.model"
}

dependencies {
  // compose stable marker
  compileOnly(libs.compose.stable.marker)

  // Kotlin Serialization for Json
  implementation(libs.kotlinx.serialization.json)

  // kotlinx
  api(libs.kotlinx.immutable.collection)
}