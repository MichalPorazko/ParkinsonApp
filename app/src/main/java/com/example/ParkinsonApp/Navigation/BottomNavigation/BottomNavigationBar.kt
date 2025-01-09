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
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: NavRoute,
    onItemClicked: (NavRoute) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = item.route == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClicked(item.route) },
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