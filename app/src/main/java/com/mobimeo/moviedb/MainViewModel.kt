package com.mobimeo.moviedb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimeo.core.model.ThemeMode
import com.mobimeo.preference.usecase.ISettingPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingPreference: ISettingPreference):ViewModel() {
	private val _selectedTheme = Channel<ThemeMode>()
	val selectedTheme = _selectedTheme.receiveAsFlow()

	fun getSelectedTheme() {
		viewModelScope.launch {
			_selectedTheme.send(settingPreference.getSelectedThemeMode(ThemeMode.DEFAULT))
		}
	}
}