package com.mobimeo.setting.di

import com.mobimeo.preference.di.SettingPreferenceModule
import com.mobimeo.preference.usecase.ISettingPreference
import com.mobimeo.setting.fake.FakeSettingPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@TestInstallIn(
	components = [SingletonComponent::class],
	replaces = [SettingPreferenceModule::class]
)
abstract class FakeSettingPreferenceModule {

	@Binds
	@Singleton
	abstract fun provideSettingPreference(sourceImpl: FakeSettingPreferenceImpl): ISettingPreference
}