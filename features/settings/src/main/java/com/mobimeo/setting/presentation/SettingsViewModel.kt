package com.mobimeo.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimeo.core.model.ThemeMode
import com.mobimeo.preference.usecase.ISettingPreference
import com.mobimeo.setting.domain.usecase.ISettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val useCase: ISettingUseCase,
	private val settingPreference: ISettingPreference
) : ViewModel() {

	private val _selectedTheme = MutableStateFlow(ThemeMode.DEFAULT)
	val selectedTheme = _selectedTheme.asStateFlow()


	fun getThemeModeData() = useCase.getAvailableThemeMode()

	fun getSelectedTheme() {
		viewModelScope.launch {
			_selectedTheme.emit(settingPreference.getSelectedThemeMode(ThemeMode.DEFAULT))
		}
	}

	fun setSelectedThemeMode(themeMode: ThemeMode) {
		viewModelScope.launch {
			settingPreference.saveThemeModePreference(themeMode)
		}
	}
}