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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.visitedcountries.designsystem.component.VisitedCountriesAutoCompleteTextField
import com.example.visitedcountries.designsystem.component.VisitedCountriesCircularProgress
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.details.R
import com.example.visitedcountries.navigation.currentComposeNavigator
import com.example.visitedcountries.preview.PreviewUtils
import com.example.visitedcountries.preview.VisitedCountriesPreviewTheme
import com.example.visitedcoutries.model.City
import kotlinx.collections.immutable.toImmutableList

@Composable
fun VisitedCountriesDetails(detailsViewModel: DetailsViewModel = hiltViewModel()) {
  val uiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
  val country by detailsViewModel.country.collectAsStateWithLifecycle()
  VisitedCountriesDetailsContent(
    uiState,
    country,
  ) { detailsViewModel.updateCity(it) }
}

@Composable
private fun VisitedCountriesDetailsContent(
  uiState: DetailsUiState,
  country: String?,
  updateCityFavorite: (City) -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("CountryDetails"),
  ) {
    DetailsHeader(
      title = country,
    )

    if (uiState is DetailsUiState.Success) {
      val cities = uiState.cityList
      VisitedCountriesAutoCompleteTextField(
        title = stringResource(R.string.search_cities_dropdown_title),
        hintText = stringResource(R.string.search_cities_hint_text),
        entries = uiState.cityList.map { it.name },
        onEntrySelected = { entry ->
          val citySelected = uiState.cityList.first { it.name == entry }
          if (!citySelected.favorite) {
            updateCityFavorite(citySelected.copy(favorite = true))
          }
        },
      )
      DetailsList(cities, updateCityFavorite)
    } else {
      Box(modifier = Modifier.fillMaxSize()) {
        VisitedCountriesCircularProgress()
      }
    }
  }
}

@Composable
private fun DetailsHeader(title: String?) {
  val composeNavigator = currentComposeNavigator
  TopAppBar(
    title = {
      Text(
        text = title.orEmpty(),
        color = VisitedCountriesTheme.colors.absoluteWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
      )
    },
    colors = TopAppBarDefaults.topAppBarColors().copy(
      containerColor = VisitedCountriesTheme.colors.primary,
    ),
    navigationIcon = {
      IconButton(onClick = {
        composeNavigator.navigateUp()
      }) {
        Icon(
          imageVector = Icons.AutoMirrored.Default.ArrowBack,
          contentDescription = "Localized description",
          tint = VisitedCountriesTheme.colors.white,
        )
      }
    },
  )
}

@Composable
private fun DetailsList(cityList: List<City>, updateCityFavorite: (City) -> Unit) {
  LazyColumn(
    modifier = Modifier.testTag("CountriesList"),
  ) {
    items(cityList) { city ->
      CityItem(city, updateCityFavorite)
    }
  }
}

@Composable
private fun CityItem(city: City, updateCityFavorite: (City) -> Unit) {
  val isFavorite = city.favorite

  Row(
    modifier = Modifier
      .padding(12.dp)
      .fillMaxWidth()
      .testTag("City"),
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.CenterVertically)
        .weight(1f),
      text = city.name,
      color = VisitedCountriesTheme.colors.black,
      textAlign = TextAlign.Start,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
    )
    IconToggleButton(
      checked = isFavorite,
      onCheckedChange = {
        updateCityFavorite(city.copy(favorite = !isFavorite))
      },
    ) {
      Icon(
        imageVector = if (isFavorite) {
          Icons.Filled.Favorite
        } else {
          Icons.Default.FavoriteBorder
        },
        contentDescription = if (isFavorite) {
          "FavoriteFilledIcon"
        } else {
          "FavoriteBorder"
        },
      )
    }
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VisitedCountriesDetailListScreen() {
  VisitedCountriesPreviewTheme {
    VisitedCountriesDetailsContent(
      uiState = DetailsUiState.Success(PreviewUtils.mockCityList().toImmutableList()),
      country = "Country",
    ) {}
  }
}
