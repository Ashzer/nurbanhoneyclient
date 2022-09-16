package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.core.compose.AlignLeft
import org.devjj.platform.nurbanhoney.core.compose.AlignRight
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.features.ui.common.DrawBottomNavigation
import org.devjj.platform.nurbanhoney.features.ui.common.MainToolBar
import javax.inject.Inject

@AndroidEntryPoint
class HomeComposeActivity
@Inject constructor() : ComponentActivity() {
    lateinit var drawerState: DrawerState
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var navigator: Navigator

    companion object {
        fun callingIntent(context: Context) = Intent(context, HomeComposeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawMain()
        }
    }

    override fun onBackPressed() {
        Log.d("drawerState.isOpen", "${drawerState.isOpen}")

        if (drawerState.isOpen) scope.launch {
            Toast
                .makeText(applicationContext, "Drawer closing", Toast.LENGTH_SHORT)
                .show()
            delay(timeMillis = 250)
            drawerState.close()
        }
        else super.onBackPressed()
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DrawMain() {
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        val contextForToast = LocalContext.current.applicationContext
        drawerState = scaffoldState.drawerState
        scope = coroutineScope
        AlignRight {
            MaterialTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    backgroundColor = Color.Blue,

                    //Align left
                    topBar = {
                        AlignLeft {
                            MainToolBar(scaffoldState.drawerState, coroutineScope)
                        }
                    },
                    bottomBar = {
                        AlignLeft {
                            DrawBottomNavigation(navigator)
                        }
                    },
                    content = { paddingValues ->
                        AlignLeft {
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
                    },
                    drawerContent = {
                        AlignLeft {
                            DrawerContent { itemLabel ->
                                Toast
                                    .makeText(contextForToast, itemLabel, Toast.LENGTH_SHORT)
                                    .show()
                                coroutineScope.launch {
                                    // delay for the ripple effect
                                    delay(timeMillis = 250)
                                    drawerState.close()
                                }
                            }
                        }
                    }
                )
            }
        }
    }


}


@Composable
private fun DrawerContent(
    itemClick: (String) -> Unit
) {

    Column() {
        AlignLeft {
            Button(
                onClick = { itemClick("Drawer Closing") },
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text("Close drawer")
            }
            Button(onClick = {}) {
                Text("Button2")
            }
        }

    }

}
