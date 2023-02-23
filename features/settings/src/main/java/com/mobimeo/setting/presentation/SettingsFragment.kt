package com.mobimeo.setting.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobimeo.core.fragment.BaseFragment
import com.mobimeo.core.util.setSafeOnClickListener
import com.mobimeo.core.model.ThemeMode
import com.mobimeo.setting.presentation.dialog.SettingSelectionOptionDialog
import com.mobimeo.core.util.ThemeHelper
import com.mobimeo.feature.setting.R
import com.mobimeo.feature.setting.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Rafiqul Hasan
 */

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingBinding>() {
	private val viewModel: SettingsViewModel by viewModels()

	override val layoutResourceId: Int
		get() = R.layout.fragment_setting

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initToolbar()
		initView()
		initClickListener()
		viewModel.getSelectedTheme()
	}

	private fun initToolbar() {
		dataBinding.layoutToolbar.toolbar.title = getString(R.string.title_setting)
		fragmentCommunicator?.setActionBar(dataBinding.layoutToolbar.toolbar, true)
	}

	private fun initView() {
		lifecycleScope.launchWhenStarted {
			viewModel.selectedTheme.collect { theme ->
				val themeMode = viewModel.getThemeModeData().find { it.themeMode == theme }
				dataBinding.llTheme.tvSettingName.text = getString(R.string.title_theme)
				dataBinding.llTheme.tvSelectedSetting.text = themeMode?.name ?: ""
			}
		}
	}

	private fun initClickListener() {
		dataBinding.llTheme.root.setSafeOnClickListener {
			val selectedText = dataBinding.llTheme.tvSelectedSetting.text
			val list =
				viewModel.getThemeModeData().filter { it.name != selectedText }.map { it.name }
			openSettingSelectionOptionDialog(list, SettingSelectionOptionDialog.TYPE_THEME)
		}
	}

	private fun openSettingSelectionOptionDialog(units: List<String>, type: String) {
		val destinationDialogUnitMeasurement = SettingsFragmentDirections
			.actionFragmentSettingToDialogUnitMeasurement(list = units.toTypedArray(), type = type)

		setFragmentResultListener(SettingSelectionOptionDialog.REQUEST_KEY) { key, bundle ->
			if (key == SettingSelectionOptionDialog.REQUEST_KEY) {
				val typeResult = bundle.getString(SettingSelectionOptionDialog.ARG_TYPE) ?: ""
				val unit = bundle.getString(SettingSelectionOptionDialog.ARG_SELECTED_SETTING) ?: ""
				updateView(typeResult, unit)
			}
		}
		findNavController().navigate(destinationDialogUnitMeasurement)
	}

	private fun updateView(type: String, unit: String) {
		if (type == SettingSelectionOptionDialog.TYPE_THEME) {
			dataBinding.llTheme.tvSelectedSetting.text = unit
			val selectedMode = viewModel.getThemeModeData().find { it.name == unit }
			selectedMode?.let {
				viewModel.setSelectedThemeMode(it.themeMode)
				changeTheme(it.themeMode)
			}

		}
	}

	private fun changeTheme(theme: ThemeMode){
		ThemeHelper.applyTheme(theme)
	}
}