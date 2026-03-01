package com.example.c37b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.c37b.ui.*
import com.example.c37b.ui.theme.C37BTheme

class MainActivity : ComponentActivity() {

    private val viewModel: RideViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            C37BTheme {
                RideApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RideApp(viewModel: RideViewModel) {
    val navController = rememberNavController()
    val currentUser by viewModel.currentUser.collectAsState()
    
    val startDestination = if (currentUser != null) "list" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { 
            LoginScreen(navController, viewModel) 
        }
        composable("list") { 
            RideListScreen(viewModel, navController) 
        }
        composable("profile") {
            ProfileScreen(viewModel, navController)
        }
        composable("my_rides") {
            MyRidesOnlyScreen(viewModel, navController)
        }
        composable("details/{rideId}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: ""
            RideDetailsScreen(rideId, viewModel, navController)
        }
        composable("add_ride") { 
            AddRideScreen(viewModel) { navController.popBackStack() }
        }
        composable("edit_ride/{rideId}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: ""
            AddRideScreen(viewModel, rideId) { navController.popBackStack() }
        }
    }
}
