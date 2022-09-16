package org.devjj.platform.nurbanhoney.features.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.material.DrawerState
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.R


@Composable
fun MainToolBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_appbar_title),
                contentDescription = null,
                modifier = Modifier.offset(x = (-20).dp)
            )
        },
        navigationIcon = {
            IconButton(
                content = {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_appbar_icon,
                        ),
                        contentDescription = null
                    )
                },
                onClick = {}
            )
        },
        actions = {
            IconButton(
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_menu),
                        contentDescription = "ddd"
                    )
                },
                onClick = {
                    scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
                },
            )
        }
    )
}