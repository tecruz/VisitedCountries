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

package com.example.visitedcountries.data.repository.home

import com.example.visitedcountries.database.CountryDao
import com.example.visitedcountries.database.entity.mapper.asDomain
import com.example.visitedcountries.database.entity.mapper.asEntity
import com.example.visitedcountries.network.Dispatcher
import com.example.visitedcountries.network.VisitedCountriesDispatchers
import com.example.visitedcountries.network.service.VisitedCountriesClient
import com.example.visitedcoutries.model.Country
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
  private val visitedCountriesClient: VisitedCountriesClient,
  private val countryDao: CountryDao,
  @Dispatcher(VisitedCountriesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : HomeRepository {

  override fun fetchCountryList(
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ): Flow<List<Country>> = flow {
    var countries = countryDao.getCountryList().asDomain()
    if (countries.isEmpty()) {
      val response = visitedCountriesClient.fetchCountryList()
      response.suspendOnSuccess {
        countries = data.data.map {
          Country(
            countryName = it.name,
            flag = it.flag,
            visited = false,
          )
        }.sortedBy { it.name }
        countryDao.insertCountryList(countries.asEntity())
        emit(countryDao.getCountryList().asDomain())
      }.onFailure { // handles the all error cases from the API request fails.
        onError(message())
      }
    } else {
      val countryList = countryDao.getCountryList().asDomain()
      emit(countryList)
    }
  }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}
