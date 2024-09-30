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

package com.example.visitedcountries.preview

import com.example.visitedcoutries.model.City
import com.example.visitedcoutries.model.Country

object PreviewUtils {

  private const val LIST_SIZE = 10

  fun mockCountry() = Country(
    countryName = "South Georgia",
    flag = "https://flagcdn.com/w320/gs.png",
    visited = false,
  )

  fun mockCountryList() = List(LIST_SIZE) {
    Country(countryName = "South Georgia$it", flag = "", visited = true)
  }

  fun mockCityList() = List(LIST_SIZE) {
    City(name = "City$it", country = "country", favorite = false)
  }
}
