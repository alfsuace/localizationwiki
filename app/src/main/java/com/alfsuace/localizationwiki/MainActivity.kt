package com.alfsuace.localizationwiki

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alfsuace.localizationwiki.localization.presentation.WikiLocalizationScreen
import com.alfsuace.localizationwiki.localization.presentation.WikiLocalizationViewModel
import com.alfsuace.localizationwiki.ui.theme.LocalizationwikiTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WikiLocalizationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LocalizationwikiTheme {
                WikiLocalizationScreen(
                    viewModel = viewModel,
                    onOpenUrl = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                )
            }
        }
    }
}