@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress(
    "OPT_IN_IS_NOT_ENABLED",
    "EXPERIMENTAL_IS_NOT_ENABLED",
    "DEPRECATION"
)

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Core

object MyView {

    @SuppressLint("ModifierFactoryExtensionFunction", "ComposableModifierFactory")
    object DialogDefaults {
        val Modifier: Modifier
            @Composable get() { return androidx.compose.ui.Modifier }
        val MinWidth = 280.dp
        val MaxWidth = 560.dp
        val MinHeight = 0.dp
        val MaxHeight = Dp.Infinity
        val ContainerPadding = PaddingValues(
            start = 24.dp,
            top = 24.dp,
            end = 24.dp,
            bottom = 18.dp
        )
        val Shape: Shape
            @Composable get() { return MaterialTheme.shapes.medium }
        val ContainerColor: Color
            @Composable get() { return MaterialTheme.colorScheme.surface }
        val TonalElevation = 2.dp
        val ListTextAlign = TextAlign.Start
        val IconContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val TitleContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val TextContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val ContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val ListContentColor: Color
            @Composable get() { return MaterialTheme.colorScheme.primary }
        val ButtonContentColor: Color
            @Composable get() { return MaterialTheme.colorScheme.primary }
        val TitleTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.headlineSmall }
        val TextTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.bodyMedium }
        val ContentTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.bodyMedium }
        val ListTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.labelLarge }
        val ButtonTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.labelLarge }
        val Properties = DialogProperties()
    }

    @Composable
    fun DialogButtonRow(
        modifier: Modifier = Modifier,
        button: @Composable RowScope.() -> Unit
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = button
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun SurfaceDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        properties: DialogProperties = DialogDefaults.Properties,
        content: @Composable () -> Unit
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                modifier = modifier.then(
                    Modifier.sizeIn(
                        minWidth = minWidth,
                        maxWidth = maxWidth,
                        minHeight = minHeight,
                        maxHeight = maxHeight
                    )
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
    fun BaseDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        content: (@Composable () -> Unit)? = null,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        contentColor: Color = DialogDefaults.ContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        contentTextStyle: TextStyle = DialogDefaults.ContentTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        SurfaceDialog(
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
            Column(
                modifier = Modifier.padding(containerPadding),
                horizontalAlignment = if (icon != null) Alignment.CenterHorizontally else Alignment.Start
            ) {

                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                        Box(
                            modifier = Modifier.padding(PaddingValues(bottom = 16.dp))
                        ) {
                            icon()
                        }
                    }
                }

                title?.let {
                    CompositionLocalProvider(LocalContentColor provides titleContentColor) {
                        ProvideTextStyle(titleTextStyle) {
                            Box(
                                modifier = Modifier.padding(PaddingValues(bottom = 16.dp))
                            ) {
                                title()
                            }
                        }
                    }
                }

                text?.let {
                    CompositionLocalProvider(LocalContentColor provides textContentColor) {
                        ProvideTextStyle(textTextStyle) {
                            Box(
                                modifier = Modifier
                                    .weight(weight = 1f, fill = false)
                                    .padding(PaddingValues(bottom = 16.dp))
                                    .align(Alignment.Start)
                            ) {
                                text()
                            }
                        }
                    }
                }

                content?.let {
                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                        ProvideTextStyle(contentTextStyle) {
                            Box(
                                modifier = Modifier
                                    .weight(weight = 1f, fill = false)
                                    .padding(PaddingValues(bottom = 16.dp))
                                    .align(Alignment.Start)
                            ) {
                                content()
                            }
                        }
                    }
                }

                button?.let {
                    CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
                        ProvideTextStyle(buttonTextStyle) {
                            Box(
                                modifier = Modifier
                                    .padding(PaddingValues(top = 2.dp))
                                    .align(Alignment.End),
                            ) {
                                button()
                            }
                        }
                    }
                }

            }
        }

    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            button = button,
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        dismissButton: (@Composable () -> Unit)? = null,
        confirmButton: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        MessageDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            button = {
                DialogButtonRow {
                    if (dismissButton != null) dismissButton()
                    if (confirmButton != null) confirmButton()
                }
            },
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        MessageDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = if (icon != null) { -> Icon(imageVector = icon, contentDescription = null) } else null,
            title = if (title != null) { -> Text(text = title) } else null,
            text = if (text != null) { -> Text(text = text) } else null,
            dismissButton = if (dismissButtonText != null && onDismissButtonClick != null)
                { ->
                    TextButton(onClick = onDismissButtonClick) {
                        Text(text = dismissButtonText)
                    }
                } else null,
            confirmButton = if (confirmButtonText != null && onConfirmButtonClick != null)
                { ->
                    TextButton(onClick = onConfirmButtonClick) {
                        Text(text = confirmButtonText)
                    }
                } else null,
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        itemCount: Int,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            items(itemCount) { index ->
                                itemContent(index)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: List<T>,
        itemContent: @Composable LazyItemScope.(item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            items(items) { item ->
                                itemContent(item)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: Array<T>,
        itemContent: @Composable LazyItemScope.(item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            itemContent = itemContent,
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: List<T>,
        itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            itemsIndexed(items) { index, item ->
                                itemContent(index, item)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: Array<T>,
        itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            itemContent = itemContent,
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        items: List<String>,
        listTextAlign: TextAlign = DialogDefaults.ListTextAlign,
        onItemClick: (index: Int, item: String) -> Unit,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = if (icon != null) { -> Icon(imageVector = icon, contentDescription = null) } else null,
            title = if (title != null) { -> Text(text = title) } else null,
            text = if (text != null) { -> Text(text = text) } else null,
            items = items,
            itemContent = { index, item ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { onItemClick(index, item) },
                        shape = RectangleShape
                    ) {
                        Text(
                            text = item,
                            textAlign = listTextAlign,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            },
            button = {
                DialogButtonRow {
                    if (dismissButtonText != null && onDismissButtonClick != null) {
                        TextButton(onClick = onDismissButtonClick) {
                            Text(text = dismissButtonText)
                        }
                    }
                    if (confirmButtonText != null && onConfirmButtonClick != null) {
                        TextButton(onClick = onConfirmButtonClick) {
                            Text(text = confirmButtonText)
                        }
                    }
                }
            },
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        items: Array<String>,
        listTextAlign: TextAlign = DialogDefaults.ListTextAlign,
        onItemClick: (index: Int, item: String) -> Unit,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            listTextAlign = listTextAlign,
            onItemClick = onItemClick,
            dismissButtonText = dismissButtonText,
            confirmButtonText = confirmButtonText,
            onDismissButtonClick = onDismissButtonClick,
            onConfirmButtonClick = onConfirmButtonClick,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
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