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

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.visitedcountries.data.repository.home.HomeRepository
import com.example.visitedcountries.viewmodel.BaseViewModel
import com.example.visitedcountries.viewmodel.ViewModelStateFlow
import com.example.visitedcoutries.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  homeRepository: HomeRepository,
) : BaseViewModel() {
  internal val uiState: ViewModelStateFlow<HomeUiState> = viewModelStateFlow(HomeUiState.Loading)

  val countryList: StateFlow<List<Country>> = homeRepository.fetchCountryList(
    onStart = { uiState.tryEmit(key, HomeUiState.Loading) },
    onComplete = { uiState.tryEmit(key, HomeUiState.Idle) },
    onError = { uiState.tryEmit(key, HomeUiState.Error(it)) },
  ).stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(),
    initialValue = emptyList(),
  )
}

@Stable
internal sealed interface HomeUiState {
  data object Idle : HomeUiState
  data object Loading : HomeUiState
  data class Error(val message: String?) : HomeUiState
}
