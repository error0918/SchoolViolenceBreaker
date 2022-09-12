@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.taeyeon.core.Core
import com.taeyeon.schoolviolencebreaker.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope

class LicenseActivity : ComponentActivity() {
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
                License.License()
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

object License {
    private lateinit var scope: CoroutineScope
    private val snackbarHostState = SnackbarHostState()

    @Composable
    fun License() {
        scope = rememberCoroutineScope()

        Scaffold(
            topBar = { Toolbar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            MainContent(paddingValues = innerPadding)
        }
    }

    @Composable
    fun Toolbar() {
        SmallTopAppBar(
            title = { Text(text = stringResource(id = R.string.license)) },
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
    fun LicenseViewer(license: com.taeyeon.core.License.License) {
        LicenseViewer(
            title = license.title,
            license = license.license,
            link = license.link
        )
    }

    @Composable
    fun LicenseViewer(
        title: String,
        license: String? = null,
        link: String? = null,
        defaultExpanded: Boolean = false
    ) {
        var expanded by rememberSaveable { mutableStateOf(defaultExpanded) }

        Column {
            TextButton(
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(0.dp)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) stringResource(id = R.string.close) else stringResource(
                            id = R.string.open
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(
                                LocalDensity.current.run { MaterialTheme.typography.labelMedium.fontSize.toPx().toDp() }
                                        + ButtonDefaults.TextButtonContentPadding.calculateTopPadding()
                                        + ButtonDefaults.TextButtonContentPadding.calculateBottomPadding()
                            )
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            if (expanded) {
                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                )

                if (license != null) {
                    SelectionContainer {
                        Text(
                            text = license,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (link != null) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            Core.getActivity().startActivity(intent)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(id = R.string.show_full))
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth())
        }
    }

    @Composable
    fun MainContent(paddingValues: PaddingValues) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(com.taeyeon.core.License.Licenses) { license ->
                LicenseViewer(
                    license = license
                )
            }
        }
    }

}