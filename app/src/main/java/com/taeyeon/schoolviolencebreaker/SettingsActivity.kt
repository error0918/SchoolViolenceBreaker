@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)
@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "EXPERIMENTAL_IS_NOT_ENABLED",
    "EXPERIMENTAL_IS_NOT_ENABLED"
)

package com.taeyeon.schoolviolencebreaker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.taeyeon.core.Core
import com.taeyeon.core.Error
import com.taeyeon.core.Info
import com.taeyeon.core.Utils
import com.taeyeon.schoolviolencebreaker.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
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
                Settings.Settings()
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


object Settings {
    private lateinit var scope: CoroutineScope
    val snackbarHostState = SnackbarHostState()

    @Composable
    fun Settings() {
        scope = rememberCoroutineScope()

        loadWithMessage()

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
            title = { Text(text = stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = {
                    Core.getActivity().finish()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.refresh),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    loadWithMessage()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(id = R.string.refresh),
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
        var isRefreshing by remember { mutableStateOf(false) }
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                loadWithMessage().also {
                    isRefreshing = false
                }
            },
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        ) {
            val settingsUnits = listOf(
                SettingsUnit.SettingsUnit(
                    title = stringResource(id = R.string.settings_title_theme),
                    settingsComposable = arrayListOf<@Composable () -> Unit>(
                        {
                            SettingsUnit.SelectThemeUnit(
                                title = stringResource(id = R.string.settings_title_dark_mode),
                                nameList = listOf(
                                    stringResource(id = R.string.settings_title_dark_mode_light_mode),
                                    stringResource(id = R.string.settings_title_dark_mode_dark_mode),
                                    stringResource(id = R.string.settings_title_dark_mode_system_settings)
                                ),
                                themeList = listOf(
                                    { content ->
                                        Theme(
                                            darkTheme = false,
                                            content = content
                                        )
                                    },
                                    { content ->
                                        Theme(
                                            darkTheme = true,
                                            content = content
                                        )
                                    },
                                    { content ->
                                        Theme(
                                            darkTheme = isSystemInDarkTheme(),
                                            content = content
                                        )
                                    }
                                ),
                                index = if (darkMode == com.taeyeon.core.Settings.LIGHT_MODE) 0 else if (darkMode == com.taeyeon.core.Settings.DARK_MODE) 1 else 2,
                                onSelected = { index ->
                                    darkMode = if (index == 0) com.taeyeon.core.Settings.LIGHT_MODE else if (index == 1) com.taeyeon.core.Settings.DARK_MODE else com.taeyeon.core.Settings.SYSTEM_MODE
                                    save()
                                }
                            )
                        },
                        {
                            SettingsUnit.SwitchUnit(
                                title = stringResource(id = R.string.settings_title_fullscreen_mode),
                                checked = fullScreenMode
                            ) { checked ->
                                fullScreenMode = checked
                                com.taeyeon.core.Settings.applyFullScreenMode()
                                scope.launch {
                                    if (snackbarHostState.currentSnackbarData == null) {
                                        val snackbarResult =
                                            snackbarHostState.showSnackbar(
                                                message = Core.getContext().resources.getString(R.string.settings_restart_message),
                                                actionLabel = Core.getContext().resources.getString(R.string.settings_title_restart_app),
                                                duration = SnackbarDuration.Short
                                            )
                                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                                            Utils.restartApp()
                                        }
                                    }
                                }
                                save()
                            }
                        },
                        {
                            SettingsUnit.SwitchUnit(
                                title = stringResource(id = R.string.settings_title_screen_always_on),
                                checked = screenAlwaysOn
                            ) { checked ->
                                screenAlwaysOn = checked
                                com.taeyeon.core.Settings.applyScreenAlwaysOn()
                                scope.launch {
                                    if (snackbarHostState.currentSnackbarData == null) {
                                        val snackbarResult =
                                            snackbarHostState.showSnackbar(
                                                message = Core.getContext().resources.getString(R.string.settings_restart_message),
                                                actionLabel = Core.getContext().resources.getString(R.string.settings_title_restart_app),
                                                duration = SnackbarDuration.Short
                                            )
                                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                                            Utils.restartApp()
                                        }
                                    }
                                }
                                save()
                            }
                        }
                    ).also {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
                            it.add(index = 1) {
                                SettingsUnit.SelectThemeUnit(
                                    title = stringResource(id = R.string.settings_title_color),
                                    nameList = listOf(
                                        stringResource(id = R.string.settings_title_color_default_mode),
                                        stringResource(id = R.string.settings_title_color_dynamic_mode)
                                    ),
                                    themeList = listOf(
                                        { content ->
                                            Theme(
                                                dynamicColor = false,
                                                content = content
                                            )
                                        },
                                        { content ->
                                            Theme(
                                                dynamicColor = true,
                                                content = content
                                            )
                                        }
                                    ),
                                    index = if (dynamicColor) 1 else 0,
                                    onSelected = { index ->
                                        dynamicColor = index == 1
                                        save()
                                    }
                                )
                            }
                        }
                    }
                ),

                SettingsUnit.SettingsUnit(
                    title = stringResource(id = R.string.settings_title_about_solve_problem),
                    settingsComposable = listOf(
                        {
                            SettingsUnit.TextUnit(title = stringResource(id = R.string.settings_title_restart_app)) {
                                Utils.restartApp()
                            }
                        },
                        {
                            SettingsUnit.TextUnit(title = stringResource(id = R.string.settings_title_initialize_app)) {
                                Utils.initializeData()
                                scope.launch {
                                    if (snackbarHostState.currentSnackbarData == null) {
                                        val snackbarResult =
                                            snackbarHostState.showSnackbar(
                                                message = Core.getContext().resources.getString(R.string.settings_restart_message),
                                                actionLabel = Core.getContext().resources.getString(R.string.settings_title_initialize_app_restart),
                                                duration = SnackbarDuration.Short
                                            )
                                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                                            Utils.restartApp()
                                        }
                                    }
                                }
                            }
                        }
                    )
                ),

                SettingsUnit.SettingsUnit(
                    title = stringResource(id = R.string.settings_title_about_application),
                    settingsComposable = listOf(
                        {
                            SettingsUnit.TextUnit(title = stringResource(id = R.string.settings_title_system_settings)) {
                                val intent =
                                    Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + Info.getPackage()))
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                Core.getActivity().startActivity(intent)
                            }
                        },
                        {
                            SettingsUnit.TextUnit(title = stringResource(id = R.string.license)) {
                                val intent =
                                    Intent(Core.getContext(), LicenseActivity::class.java)
                                Core.getActivity().startActivity(intent)
                            }
                        },
                        {
                            SettingsUnit.TextUnit(title = stringResource(id = R.string.info)) {
                                val intent =
                                    Intent(Core.getContext(), InfoActivity::class.java)
                                Core.getActivity().startActivity(intent)
                            }
                        }
                    )
                )
            )

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                items(settingsUnits) { settingsUnit ->
                    SettingsUnit.SettingsBlock(settingsUnit = settingsUnit)
                }
            }
        }
    }

    private fun loadWithMessage() {
        load()
            .also {
                scope.launch {
                    if (snackbarHostState.currentSnackbarData == null) {
                        snackbarHostState.showSnackbar(
                            message = Core.getContext().resources.getString(R.string.load_data_message),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
    }





    object SettingsUnit {

        data class SettingsUnit(
            val title: String,
            val settingsComposable: List<@Composable () -> Unit>
        )

        @Composable
        fun SettingsBlock(settingsUnit: SettingsUnit) {
            Column(modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = settingsUnit.title,
                    modifier = Modifier
                        .padding(start = 16.dp + with(LocalDensity.current) { MaterialTheme.shapes.large.topStart.toPx(Size.Unspecified, LocalDensity.current).toDp() })
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        settingsUnit.settingsComposable.forEachIndexed { index, composable ->
                            composable()
                            if (index != settingsUnit.settingsComposable.size - 1)
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }


        @Composable
        fun TextUnit(title: String, onClick: () -> Unit) {
            TextButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        @Composable
        fun SwitchUnit(
            title: String,
            checked: Boolean,
            onCheckedChange: (checked: Boolean) -> Unit
        ) {
            TextButton(
                onClick = { onCheckedChange(!checked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Switch(
                        checked = checked,
                        onCheckedChange = { onCheckedChange(!checked) },
                        modifier = Modifier
                            .height(16.8888888888888888888888888888888888.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }

        @Composable
        fun EditIntUnit(
            title: String,
            value: Int,
            valueRange: IntRange,
            onValueChange: (value: Int) -> Unit
        ) {
            var openEditValueDialog by rememberSaveable { mutableStateOf(false) }
            var openErrorValueDialog by rememberSaveable { mutableStateOf(false) }

            var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

            if (openEditValueDialog) {
                var editingValue by remember { mutableStateOf(value) }
                var editingValueString by remember { mutableStateOf(value.toString()) }

                errorMessage = null

                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    title = {
                        Text(text = stringResource(id = R.string.edit))
                    },
                    text = {
                        Column(modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)) {

                            Text(
                                text = Core.getContext().resources.getString(
                                    R.string.edit_rules_settings,
                                    "${valueRange.first} ~ ${valueRange.last}"
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (errorMessage != null) {
                                Text(
                                    text = Core.getContext().resources.getString(R.string.error_message, errorMessage ?: ""),
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            OutlinedTextField(
                                value = editingValueString,
                                onValueChange = { value ->
                                    editingValueString = value
                                    editingValue =
                                        try {
                                            val valueInt = value.toInt()

                                            errorMessage = if (valueInt < valueRange.first) {
                                                Core.getContext().resources.getString(R.string.edit_too_small_error_message)
                                            } else if (valueInt > valueRange.last) {
                                                Core.getContext().resources.getString(R.string.edit_too_big_error_message)
                                            } else {
                                                null
                                            }

                                            valueInt
                                        } catch (exception: NumberFormatException) {
                                            val error = Error(exception)

                                            errorMessage = if (error.message.indexOf("For input string") != -1) {
                                                Core.getContext().resources.getString(R.string.edit_for_input_string_error_message)
                                            } else if (error.message.indexOf("multiple points") != -1) {
                                                Core.getContext().resources.getString(R.string.edit_multiple_points_error_message)
                                            } else if (error.message.indexOf("empty String") != -1) {
                                                Core.getContext().resources.getString(R.string.edit_empty_string_error_message)
                                            } else {
                                                error.message
                                            }

                                            0
                                        }
                                },
                                textStyle = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                                shape = MaterialTheme.shapes.large,
                                label = { Text(text = stringResource(id = R.string.edit_message)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.fillMaxWidth()
                                )

                        }
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openEditValueDialog = false
                                if (errorMessage != null) {
                                    openErrorValueDialog = true
                                } else {
                                    onValueChange(editingValue)
                                }
                            },
                        ) {
                            Text(text = stringResource(id = R.string.edit))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openEditValueDialog = false
                            },
                        ) {
                            Text(text = stringResource(id = R.string.dismiss))
                        }
                    },
                    onDismissRequest = {
                        openEditValueDialog = false
                    }
                )
            }

            if (openErrorValueDialog) {
                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = stringResource(id = R.string.value_error),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    title = {
                        Text(text = stringResource(id = R.string.value_error))
                    },
                    text = {
                        Text(
                            text = Core.getContext().resources.getString(
                                R.string.value_error_message_settings,
                                errorMessage
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openErrorValueDialog = false
                                openEditValueDialog = true
                            }
                        ) {
                            Text(text = stringResource(id = R.string.value_reedit))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openErrorValueDialog = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.dismiss))
                        }
                    },
                    onDismissRequest = {
                        openErrorValueDialog = false
                    }
                )
            }

            TextButton(
                onClick = {
                    openEditValueDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Box(
                        modifier = Modifier
                            .width(55.dp)
                            .height(35.dp)
                            .align(Alignment.CenterEnd)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(percent = 100)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.4f
                                ),
                                shape = RoundedCornerShape(percent = 100)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        @Suppress("LocalVariableName")
        @Composable
        fun PreviewScreen(modifier: Modifier, scale: Float, originalHeight: Dp = 600.dp, composableClickable: Boolean = false) {
            val DEFAULT_SMALL_TOP_APP_BAR_HEIGHT = 64.dp // Verified
            val DEFAULT_SMALL_TOP_APP_BAR_PADDING = 6.dp // Not Verified
            val DEFAULT_SMALL_TOP_APP_BAR_TEXT_START_PADDING = 6.dp // Not Verified
            val DEFAULT_SMALL_TOP_APP_BAR_ICON_BUTTON_SIZE = 48.dp // Verified
            val DEFAULT_SMALL_TOP_APP_BAR_ICON_SIZE = 24.dp // Verified

            val DEFAULT_NAVIGATION_BAR_HEIGHT = 56.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_HEIGHT = 56.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_PADDING = 6.dp // Not Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_ICON_SIZE = 24.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_TEXT_HEIGHT = 21.714285.dp // Verified

            val DEFAULT_FLOATING_ACTION_BUTTON_HEIGHT = 56.dp // Verified
            val DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT = 24.dp // Verified

            Surface(
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
            ) {
                Scaffold(
                    modifier = Modifier.requiredHeight(originalHeight * scale),
                    bottomBar = {
                        Surface(
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DEFAULT_NAVIGATION_BAR_HEIGHT * scale),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val list = listOf(
                                    Icons.Filled.Image to stringResource(id = R.string.settings_preview_tab_image),
                                    Icons.Filled.Folder to stringResource(id = R.string.settings_preview_tab_file),
                                    Icons.Filled.AccountCircle to stringResource(id = R.string.settings_preview_tab_account)
                                )
                                list.forEach { item ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(DEFAULT_BOTTOM_NAVIGATION_ITEM_HEIGHT * scale)
                                            .also {
                                                if (composableClickable) it.clickable { }
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Spacer(
                                            modifier = Modifier.height(DEFAULT_BOTTOM_NAVIGATION_ITEM_PADDING * scale)
                                        )
                                        Icon(
                                            imageVector = item.first,
                                            contentDescription = item.second,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(DEFAULT_BOTTOM_NAVIGATION_ITEM_ICON_SIZE * scale)
                                        )
                                        Text(
                                            text = item.second,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.titleSmall.copy(fontSize = MaterialTheme.typography.titleSmall.fontSize * scale),
                                            modifier = Modifier.height(DEFAULT_BOTTOM_NAVIGATION_ITEM_TEXT_HEIGHT * scale)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        Button(
                            onClick = {  },
                            shape = MaterialTheme.shapes.medium.copy(
                                CornerSize(
                                    MaterialTheme.shapes.medium.topStart.toPx(
                                        with(LocalDensity.current) { DpSize(DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale, DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale).toSize() },
                                        LocalDensity.current
                                    ) * scale
                                )
                            ),
                            modifier = Modifier
                                .size(DEFAULT_FLOATING_ACTION_BUTTON_HEIGHT * scale)
                                .offset(x = 10.dp, y = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(0.dp),
                            enabled = composableClickable
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(id = R.string.settings_preview_add),
                                modifier = Modifier.size(DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale)
                            )
                        }
                    },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues.calculateTopPadding() * scale,
                                bottom = paddingValues.calculateBottomPadding() * scale,
                                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) * scale,
                                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) * scale
                            )
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DEFAULT_SMALL_TOP_APP_BAR_HEIGHT * scale)
                                    .padding(DEFAULT_SMALL_TOP_APP_BAR_PADDING * scale)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.settings_preview_title),
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = MaterialTheme.typography.titleMedium.fontSize * scale),
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = DEFAULT_SMALL_TOP_APP_BAR_TEXT_START_PADDING)
                                )
                                IconButton(
                                    onClick = {  },
                                    modifier = Modifier
                                        .size(DEFAULT_SMALL_TOP_APP_BAR_ICON_BUTTON_SIZE * scale)
                                        .align(Alignment.CenterEnd),
                                    enabled = composableClickable
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(id = R.string.settings_preview_refresh),
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(DEFAULT_SMALL_TOP_APP_BAR_ICON_SIZE * scale)
                                    )
                                }
                            }
                        }

                        for (i in 0 until 5) {
                            var cardSize by remember { mutableStateOf(IntSize.Zero) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp * scale, horizontal = 16.dp * scale)
                                    .onSizeChanged { size ->
                                        cardSize = size
                                    },
                                shape = MaterialTheme.shapes.large.copy(
                                    CornerSize(
                                        MaterialTheme.shapes.large.topStart.toPx(
                                            cardSize.toSize(),
                                            LocalDensity.current
                                        ) * scale
                                    )
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp * scale)
                                ) {
                                    var imageSize by remember { mutableStateOf(IntSize.Zero) }
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(with(LocalDensity.current) { (imageSize.width / 3).toDp() })
                                            .onSizeChanged { size ->
                                                if (imageSize == IntSize.Zero)
                                                    imageSize = size
                                            },
                                        shape = MaterialTheme.shapes.large.copy(
                                            CornerSize(
                                                MaterialTheme.shapes.large.topStart.toPx(
                                                    imageSize.toSize(),
                                                    LocalDensity.current
                                                ) * scale
                                            )
                                        ),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.settings_preview_image),
                                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * scale),
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp * scale))

                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        var textHeight by remember { mutableStateOf(IntSize.Zero) }
                                        Text(
                                            text = stringResource(id = R.string.settings_preview_image_description),
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * scale),
                                            modifier = Modifier
                                                .align(Alignment.CenterStart)
                                                .onSizeChanged { size ->
                                                    textHeight = size
                                                }
                                        )
                                        IconButton(
                                            onClick = {  },
                                            modifier = Modifier
                                                .size(with(LocalDensity.current) { textHeight.height.toDp() })
                                                .align(Alignment.CenterEnd),
                                            enabled = composableClickable
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = stringResource(id = R.string.settings_preview_edit),
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        fun SelectableThemeUnit(
            title: String,
            selected: Boolean,
            onClick: () -> Unit,
            scale: Float,
            theme: @Composable (@Composable () -> Unit) -> Unit,
            modifier: Modifier = Modifier,
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    theme {
                        PreviewScreen(
                            modifier = Modifier.fillMaxWidth(),
                            scale = scale
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Theme {
                        RadioButton(
                            selected = selected,
                            onClick = onClick,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.primary,
                                disabledSelectedColor = MaterialTheme.colorScheme.primary,
                                disabledUnselectedColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(20.dp)
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        @Composable
        fun SelectThemeUnit(
            title: String,
            nameList: List<String>,
            themeList: List<@Composable (content: @Composable () -> Unit) -> Unit>,
            index: Int,
            onSelected: (index: Int) -> Unit
        ) {
            if (nameList.size == themeList.size) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val size = nameList.size
                        for (index_ in 0 until size) {
                            SelectableThemeUnit(
                                title = nameList[index_],
                                selected = index_ == index,
                                onClick = { onSelected(index_) },
                                scale = 1f / size,
                                theme = { content ->
                                    themeList[index_](
                                        content = content
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )

                            if (index_ != size - 1) Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }

    }

}