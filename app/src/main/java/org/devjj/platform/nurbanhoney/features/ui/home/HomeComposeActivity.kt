package org.devjj.platform.nurbanhoney.features.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.features.ui.common.MainDrawer
import org.devjj.platform.nurbanhoney.features.ui.common.MainToolBar
import javax.inject.Inject

@AndroidEntryPoint
class HomeComposeActivity
@Inject constructor() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            DrawMain()
        }
    }

    @Preview
    @Composable
    fun DrawMain() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        MaterialTheme {
            Scaffold(
                drawerContent = {MainDrawer(drawerState)},
                drawerShape = customShape(),
                        topBar = { MainToolBar(drawerState) },
                backgroundColor = Color.Blue,
                content = { paddingValues ->
                    paddingValues.calculateTopPadding()
                }
            )
        }

    }


    private fun customShape() =  object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            return Outline.Rectangle(Rect(0f,0f,100f /* width */, 131f /* height */))
        }
    }
}

