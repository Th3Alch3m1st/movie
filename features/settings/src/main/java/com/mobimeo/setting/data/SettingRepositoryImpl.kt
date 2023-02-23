package com.mobimeo.setting.data

import com.mobimeo.core.model.ThemeMode
import com.mobimeo.setting.domain.model.ThemeModel
import com.mobimeo.setting.domain.repository.ISettingRepository
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class SettingRepositoryImpl @Inject constructor() :
	ISettingRepository {
	override fun getAvailableThemeMode(): List<ThemeModel> {
		return listOf(
			ThemeModel("System", themeMode = ThemeMode.DEFAULT),
			ThemeModel("Light", themeMode = ThemeMode.LIGHT),
			ThemeModel("Dark", themeMode = ThemeMode.DARK),
		)
	}
}