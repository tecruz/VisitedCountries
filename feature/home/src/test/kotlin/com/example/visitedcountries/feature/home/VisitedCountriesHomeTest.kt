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

package com.example.visitedcountries.feature.home

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.feature.home.fake.FakeHomeRepositoryImpl
import com.example.visitedcountries.navigation.LocalComposeNavigator
import com.example.visitedcountries.navigation.VisitedCountriesComposeNavigator
import com.example.visitedcountries.test.VisitedCountriesTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class VisitedCountriesHomeTest : VisitedCountriesTest() {

  @Test
  fun checkEmptyScreen() {
    setupComposeTestContent(FakeHomeRepositoryImpl.Companion.RESPONSE.EMPTY)

    composeTestRule
      .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.empty_screen_icon_content_description))
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(composeTestRule.activity.getString(R.string.empty_screen_text))
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithTag("CountriesList")
      .assertIsNotDisplayed()
  }

  @Test
  fun checkCountryVisitedScreen() {
    setupComposeTestContent(FakeHomeRepositoryImpl.Companion.RESPONSE.LIST)

    composeTestRule
      .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.empty_screen_icon_content_description))
      .assertIsNotDisplayed()

    composeTestRule
      .onNodeWithText(composeTestRule.activity.getString(R.string.empty_screen_text))
      .assertIsNotDisplayed()

    composeTestRule
      .onNodeWithTag("CountriesList")
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText("Saint Georgia")
      .assertIsDisplayed()
  }

  private fun setupComposeTestContent(response: FakeHomeRepositoryImpl.Companion.RESPONSE) {
    FakeHomeRepositoryImpl.value = response
    composeTestRule.setContent {
      CompositionLocalProvider(
        LocalComposeNavigator provides VisitedCountriesComposeNavigator(),
      ) {
        VisitedCountriesTheme {
          VisitedCountriesHome()
        }
      }
    }
  }
}
