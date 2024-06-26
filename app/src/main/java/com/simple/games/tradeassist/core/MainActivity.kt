package com.simple.games.tradeassist.core

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.core.navigation.Navigator
import com.simple.games.tradeassist.core.navigation.applicationListNavGraph
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            TradeAssistTheme {
                val navHostController = rememberNavController()

                DisposableEffect(key1 = Unit) {
                    navigator.attachController(this@MainActivity, navHostController)

                    onDispose {
                        navigator.detachController()
                    }
                }

                Box(
                    modifier = Modifier
                        .imePadding()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    NavHost(navHostController, startDestination = AppRoute.LoginRoute.route) {
                        applicationListNavGraph(navHostController)
                    }
                }
            }
        }
    }
}