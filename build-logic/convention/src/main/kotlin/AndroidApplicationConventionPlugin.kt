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

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.example.visitedcountries.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("com.example.visitedcountries.kover")
      }

      extensions.configure<BaseAppModuleExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = 34
      }

      extensions.getByType<KotlinAndroidProjectExtension>().apply {
        configureKotlinAndroid(this)
      }
    }
  }
}
