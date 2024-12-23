package com.example.ParkinsonApp.Navigation.BottomNavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ParkinsonApp.Navigation.NavRoute
import androidx.compose.ui.res.painterResource
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = BottomNavItem.patientItems

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route?.let { destination ->
            items.find { it.route::class.qualifiedName == destination }?.route
        }

        items.forEach { item ->
            val isSelected = item.route::class == currentRoute?.let { it::class }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Navigate using type-safe NavRoute
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination()) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.name
                    )
                },
                label = {
                    Text(text = item.name)
                }
            )
        }
    }
}