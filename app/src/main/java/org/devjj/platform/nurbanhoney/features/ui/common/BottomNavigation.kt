package org.devjj.platform.nurbanhoney.features.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.navigation.Navigator

@Composable
fun DrawBottomNavigation(navigator : Navigator){
    val context = LocalContext.current
    BottomAppBar() {
        BottomNavigationItem(
            selected = true,
            onClick = { navigator.showHome(context) },
            icon = {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_menu_home,
                    ),
                    contentDescription = null
                )
            },
        )
        BottomNavigationItem(
            selected = true,
            onClick = { },
            icon = {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_menu_ranking,
                    ),
                    contentDescription = null
                )
            },
        )
        BottomNavigationItem(
            selected = true,
            onClick = {},
            icon = {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_menu_profile,
                    ),
                    contentDescription = null
                )
            },
        )
    }
}