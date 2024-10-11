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

package com.example.visitedcountries.feature.details

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.feature.details.fake.FakeDetailsRepositoryImpl
import com.example.visitedcountries.navigation.LocalComposeNavigator
import com.example.visitedcountries.navigation.VisitedCountriesComposeNavigator
import com.example.visitedcountries.test.VisitedCountriesTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class VisitedCountriesDetailTest : VisitedCountriesTest() {

  @Before
  fun setupTest() {
    composeTestRule.setContent {
      CompositionLocalProvider(
        LocalComposeNavigator provides VisitedCountriesComposeNavigator(),
      ) {
        VisitedCountriesTheme {
          VisitedCountriesDetails(
            DetailsViewModel(
              savedStateHandle = SavedStateHandle(mapOf("countryName" to "USA")),
              detailsRepository = FakeDetailsRepositoryImpl(),
            ),
          )
        }
      }
    }
  }

  @Test
  fun testFavoriteIconClick() {
    composeTestRule
      .onNodeWithTag("CountryDetails")
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText("Los Angeles")
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithContentDescription("FavoriteBorder")
      .assertIsDisplayed()
      .performClick()

    composeTestRule
      .onNodeWithContentDescription("FavoriteFilledIcon")
      .assertIsDisplayed()
      .performClick()

    composeTestRule
      .onNodeWithContentDescription("FavoriteBorder")
      .assertIsDisplayed()
  }
}
