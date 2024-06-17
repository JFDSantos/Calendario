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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
        Scaffold(
            bottomBar = { BottomNavigationMenu(navController) } // Adicionando o menu inferior
        ) {
            NavHost(navController = navController, startDestination = "email") {
                composable(route = "login") {}
                composable(route = "calendar") { EventCalendarApp(navController) }
                composable(route = "email") { EmailScreen(navController) }
                composable(route = "settings") { SettingsScreen(navController) }
//                composable(
//                    route = "emailRead/{sender}/{subject}/{content}/{isReading}",
//                    arguments = listOf(
//                        navArgument("sender") { type = NavType.StringType},
//                        navArgument("subject") { type = NavType.StringType},
//                        navArgument("content") { type = NavType.StringType },
//                        navArgument("isReading") { type = NavType.StringType }
//                    )
//                ) {
//                    //val isReading = it.arguments?.getString("sender")
//                    EmailReadScreen(
//                        navController,
//                        it.arguments?.getString("sender", " ")!!,
//                        it.arguments?.getString("subject"," ")!!,
//                        it.arguments?.getString("content"," ")!!,
//                        it.arguments?.getString("isReading"," ")!!
//                    )
//                }
//
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
                        } // Assume false como padrão para isReading
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
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview1() {
    MainContent()
}