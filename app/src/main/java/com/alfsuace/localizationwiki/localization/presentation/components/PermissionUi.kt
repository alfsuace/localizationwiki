package com.alfsuace.localizationwiki.localization.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionUI(
    showGoToSettings: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit
) {
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
            Button(onClick = onOpenSettings) {
                Text("Abrir ajustes para activar permiso")
            }
        } else {
            Button(onClick = onRequestPermission) {
                Text("Permitir acceso a ubicación")
            }
        }
    }
}
