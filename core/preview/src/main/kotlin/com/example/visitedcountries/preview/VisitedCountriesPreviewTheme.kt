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

package com.example.visitedcountries.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.navigation.LocalComposeNavigator
import com.example.visitedcountries.navigation.VisitedCountriesComposeNavigator

@Composable
fun VisitedCountriesPreviewTheme(content: @Composable () -> Unit) {
  CompositionLocalProvider(
    LocalComposeNavigator provides VisitedCountriesComposeNavigator(),
  ) {
    VisitedCountriesTheme {
      content()
    }
  }
}
