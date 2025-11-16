package com.alfsuace.localizationwiki

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.alfsuace.localizationwiki.localization.presentation.WikiLocalizationScreen
import com.alfsuace.localizationwiki.localization.presentation.WikiLocalizationViewModel
import com.alfsuace.localizationwiki.ui.theme.LocalizationwikiTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WikiLocalizationViewModel by viewModel()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.updatePermissionStatus(granted, showSettingsOnDenial = true)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkInitialPermission()
        setContent {
            LocalizationwikiTheme {
                WikiLocalizationScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    onRequestPermission = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    onOpenUrl = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    },
                    onOpenSettings = { openAppSettings() },
                    onRefresh = { viewModel.fetchCoordinatesAndWikis() }
                )
            }
        }
    }

    private fun checkInitialPermission() {
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.updatePermissionStatus(isGranted, showSettingsOnDenial = false)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}