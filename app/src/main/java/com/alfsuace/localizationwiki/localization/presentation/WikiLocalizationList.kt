package com.alfsuace.localizationwiki.localization.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import com.alfsuace.localizationwiki.localization.presentation.components.WikiLocalizationHeader
import com.alfsuace.localizationwiki.localization.presentation.components.WikiLocalizationItem

@Composable
fun WikiLocalizationList(
    coords: GeoCoordinates?,
    wikis: List<WikiLocalization>,
    onOpenUrl: (String) -> Unit,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        isRefreshing = isLoading,
        onRefresh = onRefresh,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            WikiLocalizationHeader(coords)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wikis) { wiki ->
                    WikiLocalizationItem(
                        wiki = wiki,
                        userCoords = coords,
                        onClick = { wiki.url?.let(onOpenUrl) }
                    )
                }
            }
        }
    }
}