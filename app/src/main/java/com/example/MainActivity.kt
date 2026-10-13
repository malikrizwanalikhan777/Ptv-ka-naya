package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ui.screen.ChannelListScreen
import com.example.ui.screen.PlayerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ChannelViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowCompat.getInsetsController(window, window.decorView)
    controller.hide(WindowInsetsCompat.Type.systemBars())
    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val viewModel: ChannelViewModel = viewModel()
        
        NavHost(navController = navController, startDestination = "home") {
          composable("home") {
            ChannelListScreen(viewModel = viewModel, onStreamClick = { url ->
                val encodedUrl = java.net.URLEncoder.encode(url, "UTF-8")
                navController.navigate("player/$encodedUrl")
            })
          }
          composable("player/{url}") { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val url = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
            PlayerScreen(url = url)
          }
        }
      }
    }
  }
}
