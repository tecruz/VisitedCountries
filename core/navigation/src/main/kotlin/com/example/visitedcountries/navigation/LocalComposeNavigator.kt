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

package com.example.visitedcountries.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val LocalComposeNavigator: ProvidableCompositionLocal<AppComposeNavigator<VisitedCountriesScreen>> =
  compositionLocalOf {
    error(
      "No AppComposeNavigator provided! " +
        "Make sure to wrap all usages of VisitedCountries components in VisitedCountriesTheme.",
    )
  }

/**
 * Retrieves the current [AppComposeNavigator] at the call site's position in the hierarchy.
 */
val currentComposeNavigator: AppComposeNavigator<VisitedCountriesScreen>
  @Composable
  @ReadOnlyComposable
  get() = LocalComposeNavigator.current
