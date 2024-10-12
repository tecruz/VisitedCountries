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

package com.example.visitedcountries.feature.home.fake

import com.example.visitedcountries.data.repository.home.HomeRepository
import com.example.visitedcountries.test.MockUtil.mockCountry
import com.example.visitedcoutries.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeHomeRepositoryImpl @Inject constructor() : HomeRepository {

  override fun fetchCountryList(
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit,
  ): Flow<List<Country>> = flow {
    when (value) {
      RESPONSE.EMPTY -> emit(emptyList())
      RESPONSE.LIST -> emit(listOf(mockCountry().copy(visited = true)))
      RESPONSE.ERROR -> emit(emptyList())
    }
  }

  companion object {
    enum class RESPONSE {
      EMPTY,
      LIST,
      ERROR,
    }

    var value = RESPONSE.EMPTY
  }
}
