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

package com.example.visitedcountries.network

import com.example.visitedcountries.network.model.CityRequest
import com.example.visitedcountries.network.service.VisitedCountriesService
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VisitedCountriesServiceTest : ApiAbstract<VisitedCountriesService>() {

  private lateinit var service: VisitedCountriesService

  @Before
  fun initService() {
    service = createService(VisitedCountriesService::class.java)
  }

  @Test
  fun fetchCountryListFromNetworkTest() = runTest {
    enqueueResponse("/CountryResponse.json")
    val response = service.fetchCountryList()
    val responseBody = requireNotNull((response as ApiResponse.Success).data)

    assertThat(responseBody.msg, `is`("flags images retrieved"))
    assertThat(responseBody.error, `is`(false))
    assertThat(
      responseBody.data.first().flag,
      `is`("https://upload.wikimedia.org/wikipedia/commons/9/9a/Flag_of_Afghanistan.svg"),
    )
    assertThat(responseBody.data.first().name, `is`("Afghanistan"))
    assertThat(responseBody.data.size, `is`(220))
  }

  @Test
  fun fetchCityListFromNetworkTest() = runTest {
    enqueueResponse("/CountryCitiesResponse.json")
    val response = service.fetchCitiesFromCountry(CityRequest(countryName = "nigeria"))
    val responseBody = requireNotNull((response as ApiResponse.Success).data)

    assertThat(responseBody.cityList.size, `is`(87))
    assertThat(responseBody.cityList[0], `is`("Aba"))
    assertThat(responseBody.msg, `is`("cities in nigeria retrieved"))
    assertThat(responseBody.error, `is`(false))
  }
}
