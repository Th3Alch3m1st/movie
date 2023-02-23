package com.mobimeo.setting.domain.repository

import com.mobimeo.setting.domain.model.ThemeModel

/**
 * Created By Rafiqul Hasan
 */
interface ISettingRepository {
	fun getAvailableThemeMode(): List<ThemeModel>
}