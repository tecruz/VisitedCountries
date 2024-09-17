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

package com.example.visitedcountries.database.entity.mapper

import com.example.visitedcountries.database.entity.CountryEntity
import com.example.visitedcoutries.model.Country

object CountryEntityMapper : EntityMapper<List<Country>, List<CountryEntity>> {

  override fun asEntity(domain: List<Country>): List<CountryEntity> {
    return domain.map { country ->
      CountryEntity(
        name = country.name,
        flagImageUrl = country.imageUrl,
        visited = country.visited,
      )
    }
  }

  override fun asDomain(entity: List<CountryEntity>): List<Country> {
    return entity.map { countryEntity ->
      Country(
        countryName = countryEntity.name,
        flag = countryEntity.flagImageUrl,
        visited = countryEntity.visited,
      )
    }
  }
}

fun List<Country>.asEntity(): List<CountryEntity> {
  return CountryEntityMapper.asEntity(this)
}

fun List<CountryEntity>?.asDomain(): List<Country> {
  return CountryEntityMapper.asDomain(this.orEmpty())
}
