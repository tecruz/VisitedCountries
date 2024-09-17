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

package com.example.visitedcountries.database.di

import android.app.Application
import androidx.room.Room
import com.example.visitedcountries.database.CityDao
import com.example.visitedcountries.database.CountryDao
import com.example.visitedcountries.database.VisitedCountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(application: Application): VisitedCountriesDatabase {
    return Room
      .databaseBuilder(application, VisitedCountriesDatabase::class.java, "VisitedCountries.db")
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun provideCountryDao(appDatabase: VisitedCountriesDatabase): CountryDao {
    return appDatabase.countryDao()
  }

  @Provides
  @Singleton
  fun provideCityDao(appDatabase: VisitedCountriesDatabase): CityDao {
    return appDatabase.cityDao()
  }
}
