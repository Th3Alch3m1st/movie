package com.mobimeo.setting.di

import com.mobimeo.setting.domain.usecase.ISettingUseCase
import com.mobimeo.setting.domain.usecase.SettingUseCaseImpl
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
abstract class SettingUseCaseModule {

	@Binds
	@Singleton
	abstract fun provideSettingUseCaseImpl(useCaseImpl: SettingUseCaseImpl): ISettingUseCase
}