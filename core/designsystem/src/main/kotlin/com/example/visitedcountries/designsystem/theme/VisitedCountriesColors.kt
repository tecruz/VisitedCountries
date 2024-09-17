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

package com.example.visitedcountries.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.visitedcountries.designsystem.R

@Immutable
data class VisitedCountriesColors(
  val primary: Color,
  val background: Color,
  val backgroundLight: Color,
  val backgroundDark: Color,
  val absoluteWhite: Color,
  val absoluteBlack: Color,
  val white: Color,
  val white12: Color,
  val white56: Color,
  val white70: Color,
  val black: Color,
  val gray21: Color,
) {

  companion object {
    /**
     * Provides the default colors for the light mode of the app.
     *
     * @return A [VisitedCountriesColors] instance holding our color palette.
     */
    @Composable
    fun defaultDarkColors(): VisitedCountriesColors = VisitedCountriesColors(
      primary = colorResource(id = R.color.colorPrimary),
      background = colorResource(id = R.color.background_dark),
      backgroundLight = colorResource(id = R.color.background800_dark),
      backgroundDark = colorResource(id = R.color.background900_dark),
      absoluteWhite = colorResource(id = R.color.white),
      absoluteBlack = colorResource(id = R.color.black),
      white = colorResource(id = R.color.white_dark),
      white12 = colorResource(id = R.color.white_12_dark),
      white56 = colorResource(id = R.color.white_56_dark),
      white70 = colorResource(id = R.color.white_70_dark),
      black = colorResource(id = R.color.black_dark),
      gray21 = colorResource(id = R.color.gray_21),
    )

    /**
     * Provides the default colors for the light mode of the app.
     *
     * @return A [VisitedCountriesColors] instance holding our color palette.
     */
    @Composable
    fun defaultLightColors(): VisitedCountriesColors = VisitedCountriesColors(
      primary = colorResource(id = R.color.colorPrimary),
      background = colorResource(id = R.color.background),
      backgroundLight = colorResource(id = R.color.background800),
      backgroundDark = colorResource(id = R.color.background900),
      absoluteWhite = colorResource(id = R.color.white),
      absoluteBlack = colorResource(id = R.color.black),
      white = colorResource(id = R.color.white),
      white12 = colorResource(id = R.color.white_12),
      white56 = colorResource(id = R.color.white_56),
      white70 = colorResource(id = R.color.white_70),
      black = colorResource(id = R.color.black),
      gray21 = colorResource(id = R.color.gray_21),
    )
  }
}
