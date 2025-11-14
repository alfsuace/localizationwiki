package com.alfsuace.localizationwiki.localization.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization

@Composable
fun WikiLocalizationItem(
    wiki: WikiLocalization,
    userCoords: GeoCoordinates?,
    onClick: () -> Unit
) {
    val distance = remember(userCoords) {
        userCoords?.let {
            FloatArray(1).apply {
                android.location.Location.distanceBetween(
                    it.latitude,
                    it.longitude,
                    wiki.latitude,
                    wiki.longitude,
                    this
                )
            }[0] // metros
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (wiki.imageUrl != null) {
                AsyncImage(
                    model = wiki.imageUrl,
                    contentDescription = wiki.title,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No img")
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(wiki.title, style = MaterialTheme.typography.titleMedium)

                if (distance != null) {
                    Text(
                        text = "Distancia: ${"%.0f".format(distance)} m",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
