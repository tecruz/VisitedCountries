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

import app.cash.turbine.test
import com.example.visitedcountries.data.repository.home.HomeRepository
import com.example.visitedcountries.test.MainCoroutinesRule
import com.example.visitedcountries.test.MockUtil
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class HomeViewModelTest {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var repository: HomeRepository

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    repository = mock()
  }

  @Test
  fun test_ui_state_success() = runTest {
    val onCompleteCaptor = argumentCaptor<() -> Unit>()
    whenever(
      repository.fetchCountryList(
        any(),
        onCompleteCaptor.capture(),
        any(),
      ),
    ).thenReturn(flowOf(MockUtil.mockCountryList()))
    homeViewModel = HomeViewModel(repository)
    assertTrue(homeViewModel.uiState.value is HomeUiState.Loading)

    homeViewModel.countryList.test {
      val countryItem = awaitItem()[0]
      assertEquals("Saint Georgia", countryItem.name)
      assertEquals("https://flagcdn.com/w320/gs.png", countryItem.imageUrl)
      onCompleteCaptor.firstValue.invoke()
      assertTrue(homeViewModel.uiState.value is HomeUiState.Idle)
    }
  }

  @Test
  fun test_ui_state_error() = runTest {
    val onErrorCaptor = argumentCaptor<(String?) -> Unit>()
    whenever(
      repository.fetchCountryList(
        any(),
        any(),
        onErrorCaptor.capture(),
      ),
    ).thenReturn(flow { onErrorCaptor.firstValue.invoke("Error") })
    homeViewModel = HomeViewModel(repository)
    assertTrue(homeViewModel.uiState.value is HomeUiState.Loading)

    homeViewModel.countryList.test {
      awaitItem()
      assertTrue(homeViewModel.uiState.value is HomeUiState.Error)
      assertEquals("Error", (homeViewModel.uiState.value as HomeUiState.Error).message)
    }
  }
}
