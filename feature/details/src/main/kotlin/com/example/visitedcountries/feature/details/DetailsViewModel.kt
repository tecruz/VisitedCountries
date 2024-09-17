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

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.visitedcountries.data.repository.details.DetailsRepository
import com.example.visitedcountries.viewmodel.BaseViewModel
import com.example.visitedcountries.viewmodel.ViewModelStateFlow
import com.example.visitedcoutries.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
  private val detailsRepository: DetailsRepository,
  savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

  internal val uiState: ViewModelStateFlow<DetailsUiState> =
    viewModelStateFlow(DetailsUiState.Loading)

  val country = savedStateHandle.getStateFlow<String?>("countryName", null)

  init {
    viewModelScope.launch {
      country.filterNotNull().flatMapLatest { country ->
        detailsRepository.fetchCitiesInfo(
          country = country,
          onError = { uiState.tryEmit(key, DetailsUiState.Error(it)) },
        )
      }.collect { cities ->
        val cityList = sortCitiesByFavorite(cities)
        uiState.tryEmit(
          key,
          DetailsUiState.Success(cityList = cityList)
        )
      }
    }
  }

  fun updateCity(city: City) {
    viewModelScope.launch {
      detailsRepository.markCityAsVisited(
        city = city
      ).collect { cities ->
        val cityList = sortCitiesByFavorite(cities)
        uiState.tryEmit(
          key,
          DetailsUiState.Success(cityList = cityList)
        )
      }
    }
  }

  private fun sortCitiesByFavorite(cityList: List<City>): List<City> {
    return cityList.sortedByDescending { it.favorite }
  }
}

@Stable
internal sealed interface DetailsUiState {
  data class Success(val cityList: List<City>) : DetailsUiState
  data object Loading : DetailsUiState
  data class Error(val message: String?) : DetailsUiState
}
