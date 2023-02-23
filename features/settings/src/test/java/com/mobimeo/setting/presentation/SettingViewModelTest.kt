package com.mobimeo.setting.presentation

import com.mobimeo.core.model.ThemeMode
import com.mobimeo.preference.usecase.ISettingPreference
import com.mobimeo.setting.data.SettingRepositoryImpl
import com.mobimeo.setting.domain.usecase.ISettingUseCase
import com.mobimeo.testutil.returns
import com.mobimeo.testutil.shouldEqual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

/**
 * Created by Rafiqul Hasan
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {
	companion object{
		const val THEME_DATA_SIZE = 3
	}
	@get:Rule
	val dispatcherRule = com.mobimeo.testutil.MainDispatcherRule()

	@Mock
	lateinit var mockUseCase: ISettingUseCase

	@Mock
	lateinit var mockPreference: ISettingPreference

	private lateinit var sutSettingViewModel: SettingsViewModel

	@Before
	fun setUp() {
		sutSettingViewModel = SettingsViewModel(mockUseCase, mockPreference)
	}

	@Test
	fun `state is initially loading`() = runTest {
		sutSettingViewModel.selectedTheme.value shouldEqual ThemeMode.DEFAULT
	}

	@Test
	fun `get Theme Mode Data should return correct list`() = runTest {
		//Arrange
		successThemeData()

		//act
		val response = sutSettingViewModel.getThemeModeData()

		//Assert
		response.size shouldEqual THEME_DATA_SIZE
		response[0].themeMode shouldEqual ThemeMode.DEFAULT
		response[1].themeMode shouldEqual ThemeMode.LIGHT
		response[2].themeMode shouldEqual ThemeMode.DARK
	}

	@Test
	fun `get Selected ThemeMode should return correct ThemeData`() = runTest {
		//Arrange
		successSaveThemeData()

		//act
		sutSettingViewModel.getSelectedTheme()

		//Assert
		val response = sutSettingViewModel.selectedTheme.first()
		response shouldEqual ThemeMode.DARK
	}

	@Test
	fun `check argument pass correctly in setSelectedThemeMode fun`() = runTest {
		//arrange
		val acType = argumentCaptor<ThemeMode>()
		successSaveThemeData()

		//act
		sutSettingViewModel.setSelectedThemeMode(ThemeMode.DARK)
		Mockito.verify(mockPreference).saveThemeModePreference(acType.capture())

		//Assert
		acType.firstValue shouldEqual ThemeMode.DARK
	}


	private fun successThemeData() {
		runBlocking {
			mockUseCase.getAvailableThemeMode() returns SettingRepositoryImpl().getAvailableThemeMode()
		}
	}

	private fun successSaveThemeData() {
		runBlocking {
			mockPreference.getSelectedThemeMode(ThemeMode.DEFAULT) returns ThemeMode.DARK
		}
	}
}