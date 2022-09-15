package org.devjj.platform.nurbanhoney.features.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.features.ui.common.MainToolBar
import javax.inject.Inject

@AndroidEntryPoint
class HomeComposeActivity
@Inject constructor() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawMain()
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DrawMain() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        val contextForToast = LocalContext.current.applicationContext

            MaterialTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            DrawerContent { itemLabel ->
                                Toast
                                    .makeText(contextForToast, itemLabel, Toast.LENGTH_SHORT)
                                    .show()
                                coroutineScope.launch {
                                    // delay for the ripple effect
                                    delay(timeMillis = 250)
                                    scaffoldState.drawerState.close()
                                }
                            }
                        }
                    },
                    topBar = {

                        MainToolBar(scaffoldState.drawerState)

                    },
                    bottomBar = { MainToolBar(scaffoldState.drawerState) },
                    backgroundColor = Color.Blue,
                    content = { paddingValues ->

                        //MainDrawer(scaffoldState.drawerState)
                        Column() {
                            Button(onClick = { /*TODO*/ }) {
                                Text("ddddddd")
                            }
                            Button(onClick = { /*TODO*/ }) {
                                Text("ddddddd")
                            }
                            Button(onClick = { /*TODO*/ }) {
                                Text("ddddddd")
                            }
                        }

                        paddingValues.calculateTopPadding()

                    }
                )
            }

    }
}

@Composable
private fun DrawerContent(
    itemClick: (String) -> Unit
) {
    Column() {
        Button(onClick = {}) {
            Text("Button1")
        }
        Button(onClick = {}) {
            Text("Button2")
        }
    }

}
