package com.mikyegresl.valostat.features.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikyegresl.valostat.R
import com.mikyegresl.valostat.base.model.ValoStatLocale
import com.mikyegresl.valostat.common.compose.ShowLoadingState
import com.mikyegresl.valostat.ui.dimen.Padding
import com.mikyegresl.valostat.ui.theme.ValoStatTypography
import kotlinx.coroutines.flow.StateFlow

/* Supported languages:

*  en-US
*  ko-KR
*  ru-RU
*  zh-CN
*  fr-FR
*  es-ES
*  de-DE
*  ja-JP
*
* */

private const val TAG = "SettingsScreen"

@Composable
fun SettingsScreen(
    state: StateFlow<SettingsScreenState>,
    onAppLangSwitched: (ValoStatLocale) -> Unit
) {
    when (val viewState = state.collectAsStateWithLifecycle().value) {
        is SettingsScreenState.SettingsLoadingState -> {
            ShowLoadingState()
        }
        is SettingsScreenState.SettingsDataState -> {
            Scaffold(
                modifier = Modifier,
                topBar = {
                    SettingsTopBar()
                }
            ) { paddingValues ->
                SettingsScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    viewState,
                    onAppLangSwitched
                )
            }
        }
    }
}

@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.settings)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.Dp16, vertical = Padding.Dp16),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier
                .wrapContentWidth()
                .weight(1f),
            text = title,
            style = ValoStatTypography.h6
        )
    }
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    state: SettingsScreenState.SettingsDataState,
    onAppLangSwitched: (ValoStatLocale) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = Padding.Dp16
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LocaleDropdownMenu(onAppLangSwitched)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocaleDropdownMenu(
    onAppLangSwitched: (ValoStatLocale) -> Unit
) {
    val locales = mapOf(
        R.string.en to ValoStatLocale.EN,
        R.string.ru to ValoStatLocale.RU,
        R.string.kr to ValoStatLocale.KR
    ).mapKeys { stringResource(it.key) }

    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                readOnly = true,
                textStyle = ValoStatTypography.caption,
                value = stringResource(R.string.current_language),
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )
            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                locales.keys.forEach { selectionLocale ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false

                            locales[selectionLocale]?.let {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(it.title)
                                )
                                onAppLangSwitched(it)
                            }
                        },
                        content = { Text(selectionLocale) }
                    )
                }
            }
        }
    }
}