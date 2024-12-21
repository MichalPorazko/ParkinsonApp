package com.example.ParkinsonApp.Draft.BottomNavigation

import com.example.ParkinsonApp.R

sealed class BottomNavItemScreen(val route: String, val icon: Int, val title: String) {

    data object Home : BottomNavItemScreen(
        route = "home_screen",
        icon = R.drawable.home_24px,
        title = "Home"
    )

    data object Schedule : BottomNavItemScreen(
        route = "schedule_screen",
        icon = R.drawable.medication_24px,
        title = "Schedule"
    )

    data object Chat : BottomNavItemScreen(
        route = "chat_screen",
        icon = R.drawable.chat_24px,
        title = "Chat"
    )


}