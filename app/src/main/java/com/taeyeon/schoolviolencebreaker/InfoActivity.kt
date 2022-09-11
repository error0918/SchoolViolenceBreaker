@file:OptIn(ExperimentalMaterial3Api::class)

package com.taeyeon.schoolviolencebreaker

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.taeyeon.core.Core
import com.taeyeon.schoolviolencebreaker.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Core.activityCreated(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object: OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish()
                    }
                }
            )
        }

        setContent {
            Theme {
                Info.Info()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        com.taeyeon.core.Settings.applyFullScreenMode()
        com.taeyeon.core.Settings.applyScreenAlwaysOn()
    }

    override fun onRestart() {
        super.onRestart()
        Core.activityCreated(this)
    }
}

object Info {
    @Composable
    fun Info() {
        lateinit var scope: CoroutineScope
        val snackbarHostState = SnackbarHostState()

        scope = rememberCoroutineScope()

        Scaffold(
            topBar = { Toolbar() },
            snackbarHost = { SnackbarHost(Settings.snackbarHostState) }
        ) { innerPadding ->
            MainContent(paddingValues = innerPadding)
        }
    }

    @Composable
    fun Toolbar() {
        SmallTopAppBar(
            title = { Text(text = stringResource(id = R.string.info)) },
            navigationIcon = {
                IconButton(onClick = {
                    Core.getActivity().finish()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.close),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .zIndex(1f)
                .shadow(10.dp)
        )
    }

    @Composable
    fun MainContent(paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_round),
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp)
                    .padding(20.dp)
            )

            listOf(
                stringResource(id = R.string.info_name) to com.taeyeon.core.Info.getApplicationName(),
                stringResource(id = R.string.info_package) to com.taeyeon.core.Info.getPackage(),
                stringResource(id = R.string.info_version_name) to com.taeyeon.core.Info.getVersionName(),
                stringResource(id = R.string.info_version_code) to com.taeyeon.core.Info.getVersionCode().toString(),
                stringResource(id = R.string.info_maker) to com.taeyeon.core.Info.getMaker()
            ).forEach { info ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = info.first,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )

                    Text(
                        text = info.second,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.info_explanation),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = com.taeyeon.core.Info.getApplicationExplanation(),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}