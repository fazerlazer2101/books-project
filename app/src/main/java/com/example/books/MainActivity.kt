package com.example.books

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


//Helper class

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            bottomNavigationBar()
        }
    }
}
//Composables

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomNavigationBar()
{

    val navController = rememberNavController()
    //List of nav items
    val items = listOf(
        BottomNavItem(
            title = "My Books",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.Star,
            route = "0"
        ),
        BottomNavItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = "1"
        ),
        BottomNavItem(
            title = "My Books",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "2"
        )
    )

    //Remebers item that is clicked
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        icon = {

                            if (index == selectedItemIndex) {
                                Icon(item.selectedIcon, contentDescription = item.title)
                            } else {
                                Icon(item.unselectedIcon, contentDescription = item.title)
                            }

                        }
                    )

                }
            }
        },

    ){innerPadding ->

        //Nav hosts assigns routes to the composables
        NavHost(navController = navController, startDestination = "0"){
            composable(route = "0"){
                PlaylistsScreen(navController = navController, innerPadding )
            }
            composable(route = "1"){
                SearchScreen(navController)
            }
            composable(route = "2"){
                StatsScreen(navController)
            }
            composable(route = "3/{playlist_id}", arguments = listOf(
                navArgument("playlist_id")
                {
                    type = NavType.IntType
                }
            )
            ){
                val playlistId = requireNotNull(it.arguments?.getInt("playlist_id"))
                PlaylistDetailsScreen(navController, innerPadding, playlistId)
            }
        }
    }
}



