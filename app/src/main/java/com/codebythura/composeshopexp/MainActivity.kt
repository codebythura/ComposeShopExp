package com.codebythura.composeshopexp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codebythura.composeshopexp.ui.ShopExpApp
import com.codebythura.composeshopexp.ui.theme.ComposeShopExpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeShopExpTheme {
                ShopExpApp()
            }
        }
    }
}