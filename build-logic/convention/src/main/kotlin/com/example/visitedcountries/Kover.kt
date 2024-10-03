/*
 *
 *  * Designed and developed by 2024 tecruz
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.example.visitedcountries

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project

internal fun Project.configureKover(extension: KoverProjectExtension) = extension.apply {
  reports {
    filters {
      excludes {
        classes(
          "*.BuildConfig",
          "*.*Hilt*",
          "*.*Dagger*",
          "*.*MembersInjector*",
          "*.*_MembersInjector",
          "*.*_Factory*",
          "*.*_Provide*Factory*",
          "*.*_Impl*",
          "*.*$*"
        )
        packages("dagger.hilt.internal.aggregatedroot.codegen", "hilt_aggregated_deps")
        annotatedBy("androidx.compose.ui.tooling.preview.Preview")
      }
    }

    total {
      verify {
        rule("Coverage must be more than 80%") {
          minBound(80)
        }
      }
    }
  }
}