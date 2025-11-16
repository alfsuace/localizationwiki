package com.alfsuace.localizationwiki.localization.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates

@Composable
fun WikiLocalizationHeader(coords: GeoCoordinates?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Localización usada:", style = MaterialTheme.typography.titleMedium)
            if (coords != null) {
                Text("Lat: ${coords.latitude}")
                Text("Lon: ${coords.longitude}")
            } else {
                Text("Sin coordenadas")
            }
        }
    }
}
