package org.devjj.platform.nurbanhoney.features.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.R



@Composable
fun MainToolBar(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
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