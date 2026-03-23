package com.example.inhelper.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inhelper.features.eureka.EurekaScreen
import com.example.inhelper.features.rolls.RollsScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute {
    @Serializable
    data object EurekaList : ScreenRoute()
    @Serializable
    data object RollsTracker : ScreenRoute()
}

data class NavigationItem<T : ScreenRoute>(
    val route: T,
    val title: String,
    val icon: ImageVector
)

val navigationItems = listOf(
    NavigationItem(ScreenRoute.EurekaList, "Eurekas", Icons.Default.Map),
    NavigationItem(ScreenRoute.RollsTracker, "Rolls Tracker", Icons.Default.Casino)
)

@Composable
fun INHelperApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                navigationItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.EurekaList,
        ) {
            composable<ScreenRoute.EurekaList> {
                EurekaScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
            composable<ScreenRoute.RollsTracker> {
                RollsScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}
