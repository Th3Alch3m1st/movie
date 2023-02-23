package com.mobimeo.setting.domain.usecase

import com.mobimeo.setting.domain.model.ThemeModel

/**
 * Created By Rafiqul Hasan
 */
interface ISettingUseCase {
	fun getAvailableThemeMode(): List<ThemeModel>
}