package com.example.books

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun nav()
{
    val navController = rememberNavController()

    //Nav hosts
    NavHost(navController = navController, startDestination = "2"){
        composable(route = "0"){
            PlaylistsScreen(navController)
        }
        composable(route = "1"){
            SearchScreen(navController)
        }
        composable(route = "2"){
            StatsScreen(navController)
        }
    }
}