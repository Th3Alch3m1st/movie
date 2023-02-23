package com.mobimeo.preference.di

import com.mobimeo.preference.usecase.ISettingPreference
import com.mobimeo.preference.usecase.SettingPreferenceImpl
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
abstract class SettingPreferenceModule {

	@Binds
	@Singleton
	abstract fun provideSettingPreference(sourceImpl: SettingPreferenceImpl): ISettingPreference
}