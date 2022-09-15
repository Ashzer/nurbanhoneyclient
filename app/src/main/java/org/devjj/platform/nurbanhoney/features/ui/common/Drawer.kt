package org.devjj.platform.nurbanhoney.features.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun MainDrawer(drawerState: DrawerState) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
            modifier = Modifier.background(Color.Cyan),
            drawerState = drawerState,
            drawerContent = {
                Column() {
                    Button(onClick = {}, modifier=Modifier.align(Alignment.Start)) {
                        Text("Button1")
                    }
                    Button(onClick = {}, modifier=Modifier.align(Alignment.Start)) {
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
