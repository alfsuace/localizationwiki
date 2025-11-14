package com.alfsuace.localizationwiki.localization.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun WikiLocalizationScreen(
    viewModel: WikiLocalizationViewModel,
    onOpenUrl: (String) -> Unit
) {
    val activity = LocalContext.current as Activity

    var hasLocationPermission by remember { mutableStateOf(false) }
    var showGoToSettings by remember { mutableStateOf(false) }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsState().value

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
            permissionRequested = true
            if (isGranted) {
                viewModel.getCoordinatesWithLocation()
            } else {
                showGoToSettings =
                    !activity.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    )

    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            if (permissionRequested) {
                showGoToSettings =
                    !activity.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                showGoToSettings = false
            }
        } else {
            viewModel.getCoordinatesWithLocation()
        }
    }

    LaunchedEffect(uiState.coords) {
        uiState.coords?.let {
            viewModel.getNearbyWikis(it)
        }
    }

    if (!hasLocationPermission) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Permiso de ubicación necesario para mostrar artículos cercanos.")
            Spacer(modifier = Modifier.height(16.dp))

            if (showGoToSettings) {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }) {
                    Text("Abrir ajustes para activar permiso")
                }
            } else {
                Button(onClick = {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Permitir acceso a ubicación")
                }
            }
        }
    } else {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ha ocurrido un error: ${uiState.error.message}")
                }
            }

            else -> {
                WikiLocalizationList(
                    coords = uiState.coords,
                    wikis = uiState.wikis,
                    onOpenUrl = onOpenUrl,
                    modifier = Modifier.systemBarsPadding()
                )
            }
        }
    }
}
