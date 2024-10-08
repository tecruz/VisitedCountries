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

package com.example.visitedcountries.data.repository.details

import com.example.visitedcountries.database.CityDao
import com.example.visitedcountries.database.CountryDao
import com.example.visitedcountries.database.entity.CityEntity
import com.example.visitedcountries.database.entity.mapper.asDomain
import com.example.visitedcountries.database.entity.mapper.asEntity
import com.example.visitedcountries.network.Dispatcher
import com.example.visitedcountries.network.VisitedCountriesDispatchers
import com.example.visitedcountries.network.service.VisitedCountriesClient
import com.example.visitedcoutries.model.City
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
  private val visitedCountriesClient: VisitedCountriesClient,
  private val cityDao: CityDao,
  private val countryDao: CountryDao,
  @Dispatcher(VisitedCountriesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : DetailsRepository {

  override fun fetchCitiesInfo(country: String, onError: (String?) -> Unit): Flow<List<City>> = flow {
    var cityList = cityDao.getCityList(country).asDomain()
    if (cityList.isEmpty()) {
      val response = visitedCountriesClient.fetchCityList(country)
      response.suspendOnSuccess {
        if (data.cityList.isNotEmpty()) {
          cityList = data.cityList.map {
            City(
              name = it,
              country = country,
              favorite = false,
            )
          }.sortedBy { it.name }
        } else {
          cityList = listOf(
            City(
              name = country,
              country = country,
              favorite = false,
            ),
          )
        }
        cityDao.insertCityList(cityList.asEntity())
        emit(cityDao.getCityList(country).asDomain())
      }.onFailure {
        onError(message())
      }
    } else {
      emit(cityList)
    }
  }.flowOn(ioDispatcher)

  override fun markCityAsVisited(city: City): Flow<List<City>> = flow {
    val countryName = city.country
    cityDao.saveFavoritedCity(
      CityEntity(
        id = String.format("%s_%s", countryName, city.name),
        name = city.name,
        country = countryName,
        favorite = city.favorite,
      ),
    )
    val updatedCityList = cityDao.getCityList(countryName).asDomain()
    val countryEntity = countryDao.getCountryByName(countryName)
    if (updatedCityList.any { it.favorite }) {
      countryDao.saveVisitedCountry(countryEntity.copy(visited = true))
    } else {
      countryDao.saveVisitedCountry(countryEntity.copy(visited = false))
    }
    emit(updatedCityList)
  }.flowOn(ioDispatcher)
}
