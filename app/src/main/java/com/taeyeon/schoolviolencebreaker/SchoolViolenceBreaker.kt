@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED", "EXPERIMENTAL_IS_NOT_ENABLED",
    "EXPERIMENTAL_IS_NOT_ENABLED", "DEPRECATION"
)

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.graphics.Point
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import kotlinx.coroutines.*

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)
var showTip by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowTip)
var shakeToReport by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeToReport)
var shakeTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeTime)
var waitTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.WaitTime)
var reportDoubleCheck by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ReportDoubleCheck)

var isReporting by mutableStateOf(false)

fun load() {
    Settings.loadSettings()

    fullScreenMode = Settings.settingsData.FullScreenMode
    screenAlwaysOn = Settings.settingsData.ScreenAlwaysOn
    darkMode = Settings.settingsData.DarkMode
    dynamicColor = Settings.settingsData.DynamicColor
    showTip = Settings.settingsData.ShowTip
    shakeToReport = Settings.settingsData.ShakeToReport
    shakeTime = Settings.settingsData.ShakeTime
    waitTime = Settings.settingsData.WaitTime
    reportDoubleCheck = Settings.settingsData.ReportDoubleCheck
}

fun save() {
    Settings.settingsData.FullScreenMode = fullScreenMode
    Settings.settingsData.ScreenAlwaysOn = screenAlwaysOn
    Settings.settingsData.DarkMode = darkMode
    Settings.settingsData.DynamicColor = dynamicColor
    Settings.settingsData.ShowTip = showTip
    Settings.settingsData.ShakeToReport = shakeToReport
    Settings.settingsData.ShakeTime = shakeTime
    Settings.settingsData.WaitTime = waitTime
    Settings.settingsData.ReportDoubleCheck = reportDoubleCheck

    Settings.saveSettings()
}

@Suppress("DEPRECATION")
@Composable
fun Report() {
    var isDoubleChecking by remember { mutableStateOf(false) }

    if (isReporting) {
        isDoubleChecking = false

        Dialog(onDismissRequest = { isReporting = false }) {
            val size = Point()
            Core.getActivity().windowManager.defaultDisplay.getRealSize(size)
            val displayWidth = with (LocalDensity.current) { size.x.toDp() }
            Surface(
                modifier = Modifier
                    .requiredWidthIn(
                        min = if (displayWidth >= 280.dp) 280.dp else 0.dp,
                        max = if (displayWidth >= 560.dp) 560.dp else displayWidth
                    )
                    .padding(10.dp),
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "투두",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text( 
                        text = "투두$size",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "투두${with (LocalDensity.current) { size.x.toDp() }}".repeat(30),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(6.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val remainingTime = remember { mutableStateOf(waitTime) }
                    wait(rememberCoroutineScope(), remainingTime)

                    for (i in 1..10) {
                        TextButton(
                            onClick = {
                                /*TODO*/
                                isReporting = false
                                if (reportDoubleCheck) isDoubleChecking = true
                            },
                            contentPadding = PaddingValues(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "sjfslkhfs${remainingTime.value}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                if (true) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = { isReporting = false },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(vertical = 6.dp)
                    ) {
                        Text(text = "닫기")
                    }
                }
            }
        }
    }

    if (isDoubleChecking) {
        //
    }
}

fun wait(coroutineScope: CoroutineScope, remainingTime: MutableState<Int>) {
    coroutineScope.launch {
        while (remainingTime.value > 0) {
            delay(1000)
            remainingTime.value--
        }
        if (remainingTime.value < 0) remainingTime.value = 0
    }
}

object Report {

    private fun call(phone: String) {
        // TODO
    }

}

object MyView {

    @Composable
    fun DialogBase(
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        minWidth: Dp? = 280.dp,
        maxWidth: Dp? = 560.dp,
        minHeight: Dp? = null,
        maxHeight: Dp? = null,
        shape: Shape = MaterialTheme.shapes.medium,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        tonalElevation: Dp = 2.dp,
        properties: DialogProperties = DialogProperties(),
        content: @Composable () -> Unit
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            val size = Point()
            Core.getActivity().windowManager.defaultDisplay.getRealSize(size)
            val displayWidth = LocalDensity.current.run { size.x.toDp() }
            val displayHeight = LocalDensity.current.run { size.y.toDp() }
            Surface(
                modifier = modifier.then(
                    Modifier
                        .also {
                            if ((minWidth ?: 0.dp) <= (maxWidth ?: Dp.Infinity)) {
                                if (minWidth == null && maxWidth != null) {
                                    it.requiredWidthIn(
                                        max = if (displayWidth >= maxWidth) maxWidth else displayWidth
                                    )
                                } else if (minWidth != null && maxWidth == null) {
                                    it.requiredWidthIn(
                                        min = if (displayWidth >= minWidth) minWidth else 0.dp
                                    )
                                } else if (minWidth != null && maxWidth != null) {
                                    it.requiredWidthIn(
                                        min = if (displayWidth >= minWidth) minWidth else 0.dp,
                                        max = if (displayWidth >= maxWidth) maxWidth else displayWidth
                                    )
                                }
                            }
                            if ((minHeight ?: 0.dp) <= (maxHeight ?: Dp.Infinity)) {
                                if (minHeight == null && maxHeight != null) {
                                    it.requiredHeightIn(
                                        max = if (displayHeight >= maxHeight) maxHeight else displayHeight
                                    )
                                } else if (minHeight != null && maxHeight == null) {
                                    it.requiredHeightIn(
                                        min = if (displayHeight >= minHeight) minHeight else 0.dp
                                    )
                                } else if (minHeight != null && maxHeight != null) {
                                    it.requiredHeightIn(
                                        min = if (displayHeight >= minHeight) minHeight else 0.dp,
                                        max = if (displayHeight >= maxHeight) maxHeight else displayHeight
                                    )
                                }
                            }
                        }
                        .padding(10.dp)
                ),
                shape = shape,
                color = containerColor,
                tonalElevation = tonalElevation,
                content = content
            )
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun DialogFull(
        onDismissRequest: () -> Unit,
        button: RowScope.() -> Unit = {},
        modifier: Modifier = Modifier,
        minWidth: Dp? = 280.dp,
        maxWidth: Dp? = 560.dp,
        minHeight: Dp? = null,
        maxHeight: Dp? = null,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        content: (@Composable () -> Unit)? = null,
        shape: Shape = MaterialTheme.shapes.medium,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        tonalElevation: Dp = 2.dp,
        iconContentColor: Color = contentColorFor(backgroundColor = containerColor),
        titleContentColor: Color = contentColorFor(backgroundColor = containerColor),
        textContentColor: Color = contentColorFor(backgroundColor = containerColor),
        contentColor: Color = contentColorFor(backgroundColor = containerColor),
        properties: DialogProperties = DialogProperties()
    ) {
        DialogBase(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            properties = properties
        ) {
            // TODO
        }
        /*
        @Composable
@ComposableInferredTarget
public fun AlertDialog(
    onDismissRequest: () → Unit,
    confirmButton: @Composable
() → Unit,
    modifier: Modifier,
    dismissButton: @Composable()
(() → Unit)?,
    icon: @Composable()
(() → Unit)?,
    title: @Composable()
(() → Unit)?,
    text: @Composable()
(() → Unit)?,
    shape: Shape,
    containerColor: Color,
    tonalElevation: Dp,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
    properties: DialogProperties
): Unit
         */
    }

    @Composable
    fun a() {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "투두",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "투두",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "투두",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(6.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val remainingTime = remember { mutableStateOf(waitTime) }
            wait(rememberCoroutineScope(), remainingTime)

            for (i in 1..10) {
                TextButton(
                    onClick = {
                        /*TODO*/
                        isReporting = false
                        //if (reportDoubleCheck) isDoubleChecking = true
                    },
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "sjfslkhfs${remainingTime.value}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        if (true) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = { isReporting = false },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 6.dp)
            ) {
                Text(text = "닫기")
            }
        }
    }

    data class TipInformation(
        val tip: String = Core.getContext().resources.getString(R.string.tip),
        val tipImageDescription: String = tip,
        val title: String,
        val message: String,
        val closeImageDescription: String? = null,
        val onCloseButtonClick: (() -> Unit)? = null,
        val imageBitmap: ImageBitmap? = null,
        val imageBitmapDescription: String? = null,
        val actionButtonTitle: String? = null,
        val onActionButtonClick: (() -> Unit)? = null,
        val modifier: Modifier = Modifier
    )

    @Composable
    fun Tip(tipInformation: TipInformation) {
        Tip(
            tip = tipInformation.tip,
            tipImageDescription = tipInformation.tipImageDescription,
            title = tipInformation.title,
            message = tipInformation.message,
            closeImageDescription = tipInformation.closeImageDescription,
            onCloseButtonClick = tipInformation.onCloseButtonClick,
            imageBitmap = tipInformation.imageBitmap,
            imageBitmapDescription = tipInformation.imageBitmapDescription,
            actionButtonTitle = tipInformation.actionButtonTitle,
            onActionButtonClick = tipInformation.onActionButtonClick,
            modifier = tipInformation.modifier
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun Tip(
        tip: String,
        tipImageDescription: String? = null,
        title: String,
        message: String,
        closeImageDescription: String? = null,
        onCloseButtonClick: (() -> Unit)? = null,
        imageBitmap: ImageBitmap? = null,
        imageBitmapDescription: String? = null,
        actionButtonTitle: String? = null,
        onActionButtonClick: (() -> Unit)? = null,
        modifier: Modifier = Modifier
    ) {
        val hasCloseButton = onCloseButtonClick != null
        val hasImage = imageBitmap != null
        val hasAction = actionButtonTitle != null && onActionButtonClick != null

        Card(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            )
        ) {
            val cornerRadius = MaterialTheme.shapes.medium.let {
                var cornerRadius: Dp = 0.dp
                val size = Size.Unspecified
                with(LocalDensity.current) {
                    val corners = listOf(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
                    corners.forEach { corner ->
                        cornerRadius += corner.toPx(size, this).toDp() / corners.size
                    }
                }
                cornerRadius
            }
            val tipIconSize = LocalDensity.current.run { MaterialTheme.typography.labelSmall.fontSize.toPx().toDp() }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cornerRadius)
            ) {
                val (closeIconButton, tipIcon, tipText, titleText, messageText, image, actionButton) = createRefs()

                if (hasCloseButton) {
                    IconButton(
                        onClick = onCloseButtonClick!!,
                        modifier = Modifier
                            .constrainAs(closeIconButton) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = closeImageDescription
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = tipImageDescription,
                    modifier = Modifier
                        .size(tipIconSize)
                        .constrainAs(tipIcon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )

                Text(
                    text = tip,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .constrainAs(tipText) {
                            top.linkTo(tipIcon.top)
                            bottom.linkTo(tipIcon.bottom)
                            start.linkTo(tipIcon.end, margin = tipIconSize / 2)
                        }
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .constrainAs(titleText) {
                            top.linkTo(tipIcon.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                if (hasImage) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = imageBitmapDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(image) {
                                top.linkTo(titleText.bottom, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                }

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .constrainAs(messageText) {
                            top.linkTo(if (hasImage) image.bottom else titleText.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                if (hasAction) {
                    TextButton(
                        onClick = onActionButtonClick!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .constrainAs(actionButton) {
                                top.linkTo(messageText.bottom, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        contentPadding = PaddingValues()
                    ) {
                        Text(text = actionButtonTitle!!)
                    }
                }

            }
        }
    }

}