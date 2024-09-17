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

package com.example.visitedcountries.data

import app.cash.turbine.test
import com.example.visitedcountries.data.repository.home.HomeRepositoryImpl
import com.example.visitedcountries.database.CountryDao
import com.example.visitedcountries.database.entity.mapper.asEntity
import com.example.visitedcountries.network.model.CountriesResponse
import com.example.visitedcountries.network.model.CountryResponse
import com.example.visitedcountries.network.service.VisitedCountriesClient
import com.example.visitedcountries.network.service.VisitedCountriesService
import com.example.visitedcountries.test.MainCoroutinesRule
import com.example.visitedcountries.test.MockUtil.mockCountryList
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HomeRepositoryImplTest {

  private lateinit var repository: HomeRepositoryImpl
  private lateinit var client: VisitedCountriesClient
  private val service: VisitedCountriesService = mock()
  private val countryDao: CountryDao = mock()

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    client = VisitedCountriesClient(service)
    repository = HomeRepositoryImpl(client, countryDao, coroutinesRule.testDispatcher)
  }

  @Test
  fun fetchSavedCountryListFromNetworkTest() = runTest {
    val mockCountryListData = mockCountryList()
    val mockCountryResponse = CountriesResponse(
      msg = "",
      error = false,
      data = mockCountryListData.map {
        CountryResponse(
          name = it.name,
          flag = it.imageUrl,
          iso2 = "",
          iso3 = "",
        )
      },
    )

    whenever(countryDao.getCountryList()).thenReturn(emptyList(), mockCountryListData.asEntity())
    whenever(service.fetchCountryList()).thenReturn(
      ApiResponse.responseOf {
        Response.success(
          mockCountryResponse,
        )
      },
    )

    repository.fetchCountryList(
      onStart = {},
      onComplete = {},
      onError = {},
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val actualItem = awaitItem()[0]
      assertEquals("Saint Georgia", actualItem.name)
      assertEquals("https://flagcdn.com/w320/gs.png", actualItem.imageUrl)
      awaitComplete()
    }

    verify(countryDao, atLeastOnce()).getCountryList()
    verify(service, atLeastOnce()).fetchCountryList()
    verify(countryDao, atLeastOnce()).insertCountryList(mockCountryListData.asEntity())
    verifyNoMoreInteractions(service)
  }

  @Test
  fun fetchSavedCountryListFromDatabaseTest() = runTest {
    whenever(countryDao.getCountryList()).thenReturn(mockCountryList().asEntity())

    repository.fetchCountryList(
      onStart = {},
      onComplete = {},
      onError = {},
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val actualItem = awaitItem()[0]
      assertEquals("Saint Georgia", actualItem.name)
      assertEquals("https://flagcdn.com/w320/gs.png", actualItem.imageUrl)
      awaitComplete()
    }

    verify(countryDao, atLeastOnce()).getCountryList()
  }
}
