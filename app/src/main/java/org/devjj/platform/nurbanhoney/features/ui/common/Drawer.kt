package org.devjj.platform.nurbanhoney.features.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun MainDrawer(drawerState: DrawerState) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column() {
                    Button(onClick = {}) {
                        Text("Button1")
                    }
                    Button(onClick = {}) {
                        Text("Button2")
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Button(onClick = { },
                        content = { Text(text = "ddddd") })
                }
            })
    }

}
