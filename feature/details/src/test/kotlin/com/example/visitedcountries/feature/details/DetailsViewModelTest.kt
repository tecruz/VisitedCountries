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

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.visitedcountries.data.repository.details.DetailsRepository
import com.example.visitedcountries.test.MainCoroutinesRule
import com.example.visitedcountries.test.MockUtil
import com.example.visitedcoutries.model.City
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class DetailsViewModelTest {

  private lateinit var detailsViewModel: DetailsViewModel
  private lateinit var repository: DetailsRepository
  private lateinit var savedStateHandle: SavedStateHandle

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    repository = mock()
    savedStateHandle = SavedStateHandle(mapOf("countryName" to "USA"))
  }

  @Test
  fun test_parameter_saved_state_handle() = runTest {
    whenever(repository.fetchCitiesInfo(any(), any())).thenReturn(flowOf(MockUtil.mockCityList()))
    detailsViewModel = DetailsViewModel(repository, savedStateHandle)
    detailsViewModel.country.test {
      val countryName = awaitItem()
      assertEquals("USA", countryName)
    }
  }

  @Test
  fun test_ui_state_init_success() = runTest {
    whenever(repository.fetchCitiesInfo(any(), any())).thenReturn(flowOf(MockUtil.mockCityList()))
    detailsViewModel = DetailsViewModel(repository, savedStateHandle)
    detailsViewModel.uiState.test {
      val actualState = awaitItem()
      assertTrue(actualState is DetailsUiState.Success)
      actualState as DetailsUiState.Success
      val city = actualState.cityList.first()
      assertEquals("Los Angeles", city.name)
      assertEquals("USA", city.country)
      assertEquals(false, city.favorite)
    }
  }

  @Test
  fun test_ui_state_loading_error() = runTest {
    val onErrorCaptor = argumentCaptor<(String?) -> Unit>()
    whenever(
      repository.fetchCitiesInfo(
        any(),
        onError = onErrorCaptor.capture(),
      ),
    ).thenReturn(emptyFlow())
    detailsViewModel = DetailsViewModel(repository, savedStateHandle)
    detailsViewModel.uiState.test {
      val actualState = awaitItem()
      assertTrue(actualState is DetailsUiState.Loading)
      onErrorCaptor.firstValue.invoke("Error")
      val errorState = awaitItem()
      errorState as DetailsUiState.Error
      val errorMsg = errorState.message
      assertEquals("Error", errorMsg)
    }
  }

  @Test
  fun test_update_city_visited() = runTest {
    val city = City("New York", "USA", true)
    whenever(repository.fetchCitiesInfo(any(), any())).thenReturn(emptyFlow())
    whenever(repository.markCityAsVisited(city)).thenReturn(flowOf(listOf(MockUtil.mockCity(), city)))
    detailsViewModel = DetailsViewModel(repository, savedStateHandle)
    detailsViewModel.updateCity(city)
    detailsViewModel.uiState.test {
      val actualState = awaitItem()
      assertTrue(actualState is DetailsUiState.Success)
      actualState as DetailsUiState.Success
      val firstCity = actualState.cityList.first()
      assertEquals("New York", firstCity.name)
      assertEquals("USA", firstCity.country)
      assertEquals(true, firstCity.favorite)
      val secondCity = actualState.cityList[1]
      assertEquals("Los Angeles", secondCity.name)
      assertEquals("USA", secondCity.country)
      assertEquals(false, secondCity.favorite)
    }
  }
}
