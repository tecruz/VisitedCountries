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

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.visitedcountries.designsystem.component.VisitedCountriesAppBar
import com.example.visitedcountries.designsystem.component.VisitedCountriesAutoCompleteTextField
import com.example.visitedcountries.designsystem.component.VisitedCountriesCircularProgress
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme
import com.example.visitedcountries.navigation.VisitedCountriesScreen
import com.example.visitedcountries.navigation.currentComposeNavigator
import com.example.visitedcountries.preview.PreviewUtils
import com.example.visitedcountries.preview.VisitedCountriesPreviewTheme
import com.example.visitedcoutries.model.Country
import com.kmpalette.palette.graphics.Palette
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.palette.PalettePlugin
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Composable
fun VisitedCountriesHome(
  homeViewModel: HomeViewModel = hiltViewModel()
) {
  val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
  val countryList by homeViewModel.countryList.collectAsStateWithLifecycle()
  VisitedCountriesHomeContent(uiState, countryList)
}

@Composable
private fun VisitedCountriesHomeContent(
  uiState: HomeUiState, countryList: List<Country>
) {
  val composeNavigator = currentComposeNavigator
  Column(modifier = Modifier.fillMaxSize()) {
    VisitedCountriesAppBar()
    VisitedCountriesAutoCompleteTextField(title = stringResource(R.string.search_countries_dropdown_title),
      hintText = stringResource(R.string.search_countries_hint_text),
      entries = countryList.map { it.name },
      onEntrySelected = {
        composeNavigator.navigate(VisitedCountriesScreen.Details(it))
      })
    CountryList(
      uiState = uiState,
      countriesList = countryList.filter { it.visited }.toImmutableList(),
    )
  }
}

@Composable
private fun CountryList(
  uiState: HomeUiState, countriesList: ImmutableList<Country>
) {
  if (countriesList.isEmpty()) {
    EmptyContent()
  } else {
    Box(modifier = Modifier.fillMaxSize()) {
      LazyVerticalGrid(
        modifier = Modifier.testTag("CountriesList"),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp),
      ) {
        items(countriesList) { country ->
          CountryCard(country)
        }
      }

      if (uiState == HomeUiState.Loading) {
        VisitedCountriesCircularProgress()
      }
    }
  }
}

@Composable
private fun CountryCard(
  country: Country,
) {
  val composeNavigator = currentComposeNavigator
  var palette by remember { mutableStateOf<Palette?>(null) }
  val backgroundColor by palette.paletteBackgroundColor()

  Card(
    modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()
        .testTag("Country")
        .clickable {
            composeNavigator.navigate(VisitedCountriesScreen.Details(country.name))
        },
    shape = RoundedCornerShape(14.dp),
    colors = CardColors(
      containerColor = VisitedCountriesTheme.colors.background,
      contentColor = backgroundColor,
      disabledContainerColor = backgroundColor,
      disabledContentColor = backgroundColor,
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    CoilImage(
      modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(top = 20.dp)
          .size(120.dp),
      imageLoader = { ImageLoader.Builder(LocalContext.current).components { add(SvgDecoder.Factory()) }.build() },
      imageModel = { country.imageUrl },
      imageOptions = ImageOptions(contentScale = ContentScale.Inside),
      component = rememberImageComponent {
        +CrossfadePlugin()
        +ShimmerPlugin(
          Shimmer.Resonate(
            baseColor = Color.Transparent,
            highlightColor = Color.LightGray,
          ),
        )

        if (!LocalInspectionMode.current) {
          +PalettePlugin(
            imageModel = country.imageUrl,
            useCache = true,
            paletteLoadedListener = { palette = it },
          )
        }
      },
      previewPlaceholder = painterResource(
        id = com.example.visitedcountries.designsystem.R.drawable.country_preview,
      )
    )

    Text(
      modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .fillMaxWidth()
          .padding(12.dp),
      text = country.name,
      color = VisitedCountriesTheme.colors.black,
      textAlign = TextAlign.Center,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
    )
  }
}

@Composable
private fun EmptyContent() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(R.drawable.ic_world),
      contentDescription = stringResource(id = R.string.empty_screen_icon_content_description)
    )
    Text(text = stringResource(R.string.empty_screen_text))
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VisitedCountriesHomeContentListScreen() {
  VisitedCountriesPreviewTheme {
    VisitedCountriesHomeContent(
      uiState = HomeUiState.Idle, countryList = PreviewUtils.mockCountryList().toImmutableList()
    )
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VisitedCountriesHomeContentEmptyScreen() {
  VisitedCountriesPreviewTheme {
    VisitedCountriesHomeContent(
      uiState = HomeUiState.Idle, countryList = emptyList()
    )
  }
}

