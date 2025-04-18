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
  id("com.example.visitedcountries.android.hilt")
}

android {
    namespace = "com.example.visitedcountries.data"
}

dependencies {
  // core modules
  api(projects.core.model)
  implementation(projects.core.network)
  implementation(projects.core.database)
  testImplementation(projects.core.test)

  // coroutines
  implementation(libs.kotlinx.coroutines.android)
  testImplementation(libs.kotlinx.coroutines.test)

  // network
  implementation(libs.sandwich)

  // kotlinx
  api(libs.kotlinx.immutable.collection)

  // unit test
  testImplementation(libs.junit)
  testImplementation(libs.turbine)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
}