package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fivedaysweather.FiveDaysScreen
import com.example.currentweather.TodayScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

fun requestLocationPermission(
    activity: ComponentActivity,
    onGranted: () -> Unit
):Boolean {
    val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onGranted()
        }
    }

    if (checkLocationPermission(activity)) {
        return true
    } else {
        // Запрашиваем разрешение
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        return false
    }
}

fun checkLocationPermission(activity: ComponentActivity): Boolean {
    return ActivityCompat.checkSelfPermission(
        activity,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var screenProvider: ScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: MainViewModel by viewModels()
        
        var locationAllowed: Boolean
        locationAllowed = requestLocationPermission(this@MainActivity){
            locationAllowed = true
            setContent {
                ScreenHolder(vm, locationAllowed, screenProvider.screens)
            }
        }

        enableEdgeToEdge()
        setContent {
            ScreenHolder(vm, locationAllowed, screenProvider.screens)
        }
    }
}


@Composable
fun ScreenHolder(vm: MainViewModel, locationAllowed: Boolean, items: List<Screen>){
    var bgColor by remember { mutableStateOf(Color.LightGray) }
    val animatedBgColor by animateColorAsState(targetValue = bgColor)

    val navController = rememberNavController()
    
    WeatherAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(backgroundColor = White) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.text) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            if(locationAllowed) {
                NavHost(navController = navController, startDestination = "current"){
                    composable(route = Routes.CURRENT_WEATHER) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = animatedBgColor)
                        ) {
                            TodayScreen(
                                modifier = Modifier.padding(innerPadding),
                                changeBackground = { color ->
                                    bgColor = color
                                }
                            )
                        }
                    }
                    composable(route = Routes.FIVE_DAY_WEATHER) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = animatedBgColor)
                        ) {
                            FiveDaysScreen(
                                modifier = Modifier.padding(innerPadding),
                                changeBackground = { color ->
                                    bgColor = color
                                }
                            )
                        }
                    }
                    composable(route = Routes.SEARCH_WEATHER) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = animatedBgColor)
                        ) {
                            com.example.searchweather.SearchScreen(
                                modifier = Modifier.padding(innerPadding),
                                changeBackground = { color ->
                                    bgColor = color
                                }
                            )
                        }
                    }
                }
            } else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.require_permissons_location),
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}