package com.mobimeo.preference.usecase

import com.mobimeo.preference.datastore.PreferenceDataStoreConstants
import com.mobimeo.preference.datastore.domain.IPreferenceDataStoreAPI
import com.mobimeo.core.model.ThemeMode
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */

class SettingPreferenceImpl @Inject constructor(private val preference: IPreferenceDataStoreAPI) :
	ISettingPreference {
	override suspend fun getSelectedThemeMode(default: ThemeMode): ThemeMode {
		val value = preference.getFirstPreference(
			PreferenceDataStoreConstants.THEME_PREF_KEY,
			default.name
		)
		return try {
			ThemeMode.valueOf(value)
		} catch (ex: Exception) {
			ThemeMode.DEFAULT
		}
	}

	override suspend fun saveThemeModePreference(themeMode: ThemeMode) {
		preference.putPreference(PreferenceDataStoreConstants.THEME_PREF_KEY, themeMode.name)
	}
}