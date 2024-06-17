package br.com.fiap.testecalendario

import EmailScreen
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.testecalendario.ui.theme.TesteCalendarioTheme

data class NavItem(val title: String, val icon: ImageVector, val route: String)
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent() {
    val navController = rememberNavController()
    TesteCalendarioTheme {
        var showBottomBar by remember { mutableStateOf(true) }

        Scaffold(

            bottomBar = { if (showBottomBar) {BottomNavigationMenu(navController)} }
        ) {
            NavHost(navController = navController, startDestination = "login") {
                composable(route = "login") {
                    showBottomBar = false
                    LoginScreen(navController)}
                composable(route = "calendar") { showBottomBar = true
                    EventCalendarApp(navController) }
                composable(route = "email") {showBottomBar = true
                    EmailScreen(navController) }
                composable(route = "settings") { showBottomBar = true
                    SettingsScreen(navController) }
                composable(
                    route = "emailRead?sender={sender}&subject={subject}&content={content}&isImportant={isImportant}&isFavorite={isFavorite}&isReading={isReading}",
                    arguments = listOf(
                        navArgument("sender") { defaultValue = "" },
                        navArgument("subject") { defaultValue = "" },
                        navArgument("content") { defaultValue = "" },
                        navArgument("isImportant") { defaultValue = "" },
                        navArgument("isFavorite") { defaultValue = "" },
                        navArgument("isReading") {
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry ->
                    val sender = backStackEntry.arguments?.getString("sender") ?: ""
                    val subject = backStackEntry.arguments?.getString("subject") ?: ""
                    val content = backStackEntry.arguments?.getString("content") ?: ""
                    val isImportant = backStackEntry.arguments?.getString("isImportant") ?: ""
                    val isFavorite = backStackEntry.arguments?.getString("isFavorite") ?: ""
                    val isReading = backStackEntry.arguments?.getString("isReading") ?: ""

                    EmailReadScreen(
                        navController = navController,
                        sender = sender,
                        subject = subject,
                        content = content,
                        isImportant = isImportant,
                        isFavorite = isFavorite,
                        isReading = isReading
                    )
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
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .height(55.dp),
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.fillMaxHeight(),
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(30.dp)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {

                        launchSingleTop = true

                        restoreState = true

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
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuração",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(text = "Sair")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview1() {
    var nav = rememberNavController()
    SettingsScreen(nav)
}