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

package com.example.visitedcountries.database

import com.example.visitedcountries.database.entity.mapper.asEntity
import com.example.visitedcountries.test.MockUtil.mockCountry
import com.example.visitedcountries.test.MockUtil.mockCountryList
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class CountryDaoTest : LocalDatabase() {

  private lateinit var countryDao: CountryDao

  @Before
  fun init() {
    countryDao = db.countryDao()
  }

  @Test
  fun insertAndLoadCountryListTest() = runBlocking {
    val mockDataList = mockCountryList().asEntity()
    countryDao.insertCountryList(mockDataList)

    val loadFromDB = countryDao.getCountryList()
    assertThat(loadFromDB.toString(), `is`(mockDataList.toString()))

    val mockData = listOf(mockCountry()).asEntity()[0]
    assertThat(loadFromDB[0].toString(), `is`(mockData.toString()))
  }
}
