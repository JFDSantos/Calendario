package br.com.fiap.testecalendario

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.testecalendario.ui.theme.TesteCalendarioTheme

data class NavItem(val title: String, val icon: ImageVector, val route: String)
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TesteCalendarioTheme {
                Scaffold(
                    bottomBar = { BottomNavigationMenu(navController) } // Adicionando o menu inferior
                ) {
                    NavHost(navController = navController, startDestination = "email") {
                        composable(route = "login") {}
                        composable(route = "calendar") { EventCalendarApp(navController) }
                        composable(route = "email") { EmailScreen(navController) }
                        composable(route = "settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}

@RequiresApi(26)
@Composable
fun BottomNavigationMenu(navController: NavController) {
    val navItems = listOf(
        NavItem("Calendário", Icons.Default.DateRange, "calendar"),
        NavItem("Email", Icons.Default.Email, "email"),
        NavItem("Configurações", Icons.Default.Settings, "settings")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        Modifier.background(MaterialTheme.colorScheme.primary),
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    // Implementation of the settings screen
    Text(text = "Configurações")
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview1() {
}