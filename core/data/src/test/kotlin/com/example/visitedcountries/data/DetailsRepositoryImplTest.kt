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
import com.example.visitedcountries.data.repository.details.DetailsRepositoryImpl
import com.example.visitedcountries.database.CityDao
import com.example.visitedcountries.database.CountryDao
import com.example.visitedcountries.database.entity.CityEntity
import com.example.visitedcountries.database.entity.CountryEntity
import com.example.visitedcountries.database.entity.mapper.asEntity
import com.example.visitedcountries.network.model.CitiesResponse
import com.example.visitedcountries.network.service.VisitedCountriesClient
import com.example.visitedcountries.network.service.VisitedCountriesService
import com.example.visitedcountries.test.MainCoroutinesRule
import com.example.visitedcountries.test.MockUtil.mockCity
import com.example.visitedcountries.test.MockUtil.mockCityList
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DetailsRepositoryImplTest {

  private lateinit var repository: DetailsRepositoryImpl
  private lateinit var client: VisitedCountriesClient
  private val service: VisitedCountriesService = mock()
  private val countryDao: CountryDao = mock()
  private val cityDao: CityDao = mock()

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    client = VisitedCountriesClient(service)
    repository = DetailsRepositoryImpl(client, cityDao, countryDao, coroutinesRule.testDispatcher)
  }

  @Test
  fun fetchCitiesFromNetworkTest() = runTest {
    val countryName = "USA"
    val cityList = mockCityList()
    val cityListEntity = cityList.asEntity()
    val mockCitiesResponse = CitiesResponse(
      msg = "",
      error = false,
      cityList = cityList.map {
        it.name
      },
    )

    whenever(cityDao.getCityList(countryName)).thenReturn(emptyList())
    whenever(service.fetchCitiesFromCountry(any())).thenAnswer {
      ApiResponse.responseOf {
        Response.success(
          mockCitiesResponse,
        )
      }
    }

    repository.fetchCitiesInfo(
      countryName,
      onError = {},
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      whenever(cityDao.getCityList(countryName)).thenReturn(cityListEntity)
      awaitItem()
      awaitComplete()
    }

    inOrder(cityDao, service) {
      verify(cityDao).getCityList(countryName)
      verify(service).fetchCitiesFromCountry(any())

      val cityListInsertArgumentCaptor = argumentCaptor<List<CityEntity>>()
      verify(cityDao).insertCityList(cityListInsertArgumentCaptor.capture())

      val cityListInserted = cityListInsertArgumentCaptor.firstValue
      assertEquals(1, cityListInserted.size)
      assertEquals("USA_Los Angeles", cityListInserted[0].id)
      assertEquals("Los Angeles", cityListInserted[0].name)
      assertEquals("USA", cityListInserted[0].country)
      assertEquals(false, cityListInserted[0].favorite)

      verify(cityDao).getCityList(countryName)
      verifyNoMoreInteractions(cityDao)
    }
  }

  @Test
  fun fetchCitiesFromNetworkTestEmptyResponse() = runTest {
    val countryName = "Monaco"
    val mockCitiesResponse = CitiesResponse(
      msg = "",
      error = false,
      cityList = emptyList(),
    )

    whenever(cityDao.getCityList(countryName)).thenReturn(emptyList())
    whenever(service.fetchCitiesFromCountry(any())).thenAnswer {
      ApiResponse.responseOf {
        Response.success(
          mockCitiesResponse,
        )
      }
    }

    repository.fetchCitiesInfo(
      countryName,
      onError = {},
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      awaitItem()
      awaitComplete()
    }

    inOrder(cityDao, service) {
      verify(cityDao).getCityList(countryName)
      verify(service).fetchCitiesFromCountry(any())

      val cityListInsertArgumentCaptor = argumentCaptor<List<CityEntity>>()
      verify(cityDao).insertCityList(cityListInsertArgumentCaptor.capture())

      val cityListInserted = cityListInsertArgumentCaptor.firstValue
      assertEquals(1, cityListInserted.size)
      assertEquals("Monaco_Monaco", cityListInserted[0].id)
      assertEquals("Monaco", cityListInserted[0].name)
      assertEquals("Monaco", cityListInserted[0].country)
      assertEquals(false, cityListInserted[0].favorite)

      verify(cityDao).getCityList(countryName)
      verifyNoMoreInteractions(cityDao)
    }
  }

  @Test
  fun fetchCitiesFromDatabaseTest() = runTest {
    val countryName = "USA"
    whenever(cityDao.getCityList(countryName)).thenReturn(mockCityList().asEntity())
    repository.fetchCitiesInfo(
      countryName,
      onError = {},
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val actualItem = awaitItem()[0]
      assertEquals("Los Angeles", actualItem.name)
      assertEquals("USA", actualItem.country)
      assertEquals(false, actualItem.favorite)
      awaitComplete()
    }

    verify(cityDao).getCityList(countryName)
    verifyNoMoreInteractions(cityDao)
  }

  @Test
  fun fetchCitiesFromNetworkError() = runTest {
    val countryName = "USA"
    whenever(cityDao.getCityList(countryName)).thenReturn(emptyList())
    whenever(service.fetchCitiesFromCountry(any())).thenAnswer {
      ApiResponse.responseOf {
        Response.error<String>(
          500,
          "".toResponseBody(),
        )
      }
    }

    val onErrorCallback = mock<(String?) -> Unit>()
    val errorCaptor = argumentCaptor<String>()
    repository.fetchCitiesInfo(
      countryName,
      onError = onErrorCallback,
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      awaitComplete()
      verify(onErrorCallback).invoke(errorCaptor.capture())
      val errorResponse = errorCaptor.firstValue
      assertEquals(
        "Response{protocol=http/1.1, code=500, message=Response.error(), url=http://localhost/}",
        errorResponse,
      )
    }
  }

  @Test
  fun updateCityMarkCountryAsNotVisited() = runTest {
    val countryName = "USA"
    val mockCity = mockCity()
    whenever(cityDao.getCityList(countryName)).thenReturn(listOf(mockCity).asEntity())
    whenever(countryDao.getCountryByName(countryName)).thenReturn(
      CountryEntity(
        name = countryName,
        flagImageUrl = "",
        visited = false,
      ),
    )

    repository.markCityAsVisited(
      mockCity,
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val actualItem = awaitItem()[0]
      assertEquals("Los Angeles", actualItem.name)
      assertEquals("USA", actualItem.country)
      assertEquals(false, actualItem.favorite)
      awaitComplete()
    }

    inOrder(cityDao, countryDao) {
      val cityEntityArgumentCaptor = argumentCaptor<CityEntity>()
      verify(cityDao).saveFavoritedCity(cityEntityArgumentCaptor.capture())

      assertEquals(false, cityEntityArgumentCaptor.firstValue.favorite)

      verify(cityDao).getCityList(countryName)
      verifyNoMoreInteractions(cityDao)
      verify(countryDao).getCountryByName(countryName)
      val countryEntityArgumentCaptor = argumentCaptor<CountryEntity>()
      verify(countryDao).saveVisitedCountry(countryEntityArgumentCaptor.capture())

      assertEquals(false, countryEntityArgumentCaptor.firstValue.visited)

      verifyNoMoreInteractions(countryDao)
    }
  }

  @Test
  fun updateCityMarkCountryAsVisited() = runTest {
    val countryName = "USA"
    val mockCity = mockCity().copy(favorite = true)
    whenever(cityDao.getCityList(countryName)).thenReturn(listOf(mockCity).asEntity())
    whenever(countryDao.getCountryByName(countryName)).thenReturn(
      CountryEntity(
        name = countryName,
        flagImageUrl = "",
        visited = false,
      ),
    )

    repository.markCityAsVisited(
      mockCity,
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val actualItem = awaitItem()[0]
      assertEquals("Los Angeles", actualItem.name)
      assertEquals("USA", actualItem.country)
      assertEquals(true, actualItem.favorite)
      awaitComplete()
    }

    inOrder(cityDao, countryDao) {
      val cityEntityArgumentCaptor = argumentCaptor<CityEntity>()

      verify(cityDao).saveFavoritedCity(cityEntityArgumentCaptor.capture())
      assertEquals(true, cityEntityArgumentCaptor.firstValue.favorite)

      verify(cityDao).getCityList(countryName)
      verifyNoMoreInteractions(cityDao)
      verify(countryDao).getCountryByName(countryName)

      val countryEntityArgumentCaptor = argumentCaptor<CountryEntity>()
      verify(countryDao).saveVisitedCountry(countryEntityArgumentCaptor.capture())

      assertEquals(true, countryEntityArgumentCaptor.firstValue.visited)

      verifyNoMoreInteractions(countryDao)
    }
  }
}
