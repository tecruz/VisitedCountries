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

package com.example.visitedcountries.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.visitedcountries.designsystem.R
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme

@Composable
fun VisitedCountriesAppBar() {
  TopAppBar(
    title = {
      Text(
        text = stringResource(id = R.string.app_name),
        color = VisitedCountriesTheme.colors.absoluteWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
      )
    },
    colors = TopAppBarDefaults.topAppBarColors().copy(
      containerColor = VisitedCountriesTheme.colors.primary,
    ),
  )
}

@Preview
@Composable
private fun VisitedCountriesBarPreview() {
  VisitedCountriesTheme {
    VisitedCountriesAppBar()
  }
}
