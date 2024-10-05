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

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.test.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class VisitedCountriesNavigationTest {

  @get:Rule(order = 1)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2)
  val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

  private lateinit var navController: TestNavHostController

  @Before
  fun setupAppNavHost() {
    hiltRule.inject()
    composeTestRule.setContent {
      CompositionLocalProvider(
        LocalComposeNavigator provides VisitedCountriesComposeNavigator(),
      ) {
        VisitedCountriesTheme {
          navController = TestNavHostController(LocalContext.current)
          navController.navigatorProvider.addNavigator(ComposeNavigator())
          VisitedCountriesNavHost(navController)
        }
      }
    }
  }

  @Test
  fun appNavHost_verifyStartDestination() {
    composeTestRule
      .onNodeWithText("Visited Countries")
      .assertIsDisplayed()
  }
}
