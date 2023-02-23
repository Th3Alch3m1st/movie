package com.mobimeo.preference.usecase

import com.mobimeo.core.model.ThemeMode

/**
 * Created By Rafiqul Hasan
 */
interface ISettingPreference {
	suspend fun getSelectedThemeMode(default: ThemeMode): ThemeMode
	suspend fun saveThemeModePreference(themeMode: ThemeMode)
}