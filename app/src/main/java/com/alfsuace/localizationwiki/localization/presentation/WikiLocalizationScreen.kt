package com.alfsuace.localizationwiki.localization.presentation

import androidx.compose.runtime.Composable
import com.alfsuace.localizationwiki.localization.presentation.components.ErrorUI
import com.alfsuace.localizationwiki.localization.presentation.components.LoadingUI
import com.alfsuace.localizationwiki.localization.presentation.components.PermissionUI

@Composable
fun WikiLocalizationScreen(
    uiState: WikiLocalizationViewModel.WikiLocalizationUiState,
    onRequestPermission: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onRefresh: () -> Unit
) {
    when {
        !uiState.hasLocationPermission -> PermissionUI(
            showGoToSettings = uiState.showGoToSettings,
            onRequestPermission = onRequestPermission,
            onOpenSettings = onOpenSettings
        )

        uiState.isLoading -> LoadingUI()

        uiState.error != null -> ErrorUI(uiState.error)

        else -> WikiLocalizationList(
            coords = uiState.coords,
            wikis = uiState.wikis,
            onOpenUrl = onOpenUrl,
            isLoading = uiState.isLoading,
            onRefresh = onRefresh
        )
    }
}