package com.mobimeo.setting.fake

import com.mobimeo.core.model.ThemeMode
import com.mobimeo.preference.usecase.ISettingPreference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rafiqul Hasan
 */
@Singleton
class FakeSettingPreferenceImpl @Inject constructor() : ISettingPreference {
	override suspend fun getSelectedThemeMode(default: ThemeMode): ThemeMode = ThemeMode.DARK

	override suspend fun saveThemeModePreference(themeMode: ThemeMode) {

	}
}