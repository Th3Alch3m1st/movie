package com.mobimeo.setting.domain.usecase

import com.mobimeo.setting.domain.model.ThemeModel
import com.mobimeo.setting.domain.repository.ISettingRepository
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class SettingUseCaseImpl @Inject constructor(private val repository: ISettingRepository) :
	ISettingUseCase {
	override fun getAvailableThemeMode(): List<ThemeModel> {
		return repository.getAvailableThemeMode()
	}
}