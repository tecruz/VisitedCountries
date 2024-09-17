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

import com.example.visitedcountries.database.entity.CityEntity
import com.example.visitedcoutries.model.City

object CityEntityMapper : EntityMapper<List<City>, List<CityEntity>> {

  override fun asEntity(domain: List<City>): List<CityEntity> {
    return domain.map { city ->
      CityEntity(
        id = String.format("%s_%s", city.country, city.name),
        name = city.name,
        country = city.country,
        favorite = city.favorite,
      )
    }
  }

  override fun asDomain(entity: List<CityEntity>): List<City> {
    return entity.map { cityEntity ->
      City(
        name = cityEntity.name,
        country = cityEntity.country,
        favorite = cityEntity.favorite,
      )
    }
  }
}

fun List<City>.asEntity(): List<CityEntity> {
  return CityEntityMapper.asEntity(this)
}

fun List<CityEntity>?.asDomain(): List<City> {
  return CityEntityMapper.asDomain(this.orEmpty())
}
