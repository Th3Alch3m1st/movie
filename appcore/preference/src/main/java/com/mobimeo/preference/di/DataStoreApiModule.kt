package com.mobimeo.preference.di

import com.mobimeo.preference.datastore.PreferenceDataStoreAPIImpl
import com.mobimeo.preference.datastore.domain.IPreferenceDataStoreAPI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreApiModule {

	@Binds
	@Singleton
	abstract fun providePreferenceDataStoreApi(sourceImpl: PreferenceDataStoreAPIImpl): IPreferenceDataStoreAPI
}