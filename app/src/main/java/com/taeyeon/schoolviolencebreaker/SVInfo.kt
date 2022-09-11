@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Utils

object SVInfo {

    @Composable
    fun SVInfo(paddingValues: PaddingValues = PaddingValues()) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 16.dp ,
                bottom = paddingValues.calculateBottomPadding() + 16.dp,
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Tip(
                tip = "asfddfsa",
                tipImageDescription = "",
                title = "adsf",
                message = "adfs",
                onCloseButtonClick = {
                    Utils.toast("TODO")
                },
                closeImageDescription = "daf",
                imageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_launcher_round),
                imageBitmapDescription = "",
                actionTitle = "sasdfA",
                actionClick = {
                    Utils.toast("adsf")
                }
            )
            Text(
                modifier = Modifier.fillMaxSize(),
                text = "정태연 ".repeat(100)
            )
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun Tip(
        tip: String,
        tipImageDescription: String? = null,
        title: String,
        message: String,
        onCloseButtonClick: (() -> Unit)? = null,
        closeImageDescription: String? = null,
        imageBitmap: ImageBitmap? = null,
        imageBitmapDescription: String? = null,
        actionTitle: String? = null,
        actionClick: (() -> Unit)? = null,
        modifier: Modifier = Modifier
    ) { //TODO
        val hasCloseButton = onCloseButtonClick != null
        val hasImage = imageBitmap != null && imageBitmapDescription != null
        val hasAction = actionTitle != null && actionClick != null

        Card(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            )
        ) {
            var cornerRadius: Dp = 0.dp
            MaterialTheme.shapes.medium.let {
                val size = Size.Unspecified
                with(LocalDensity.current) {
                    val corners = listOf(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
                    corners.forEach { corner ->
                        cornerRadius += corner.toPx(size, this).toDp() / corners.size
                    }
                }
            }
            val tipIconSize = MaterialTheme.typography.labelSmall.fontSize.value.dp

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cornerRadius)
            ) {
                val (closeIconButton, tipIcon, tipText, titleText, text, image, actionButton) = createRefs()

                if (hasCloseButton) {
                    IconButton(
                        onClick = { /*TODO*/ },
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
                    text = "팁",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .constrainAs(tipText) {
                            top.linkTo(tipIcon.top)
                            bottom.linkTo(tipIcon.bottom)
                            start.linkTo(tipIcon.end, margin = tipIconSize / 2)
                        }
                )

                Text(
                    text = "타이틀",
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
                    text = "설명 ".repeat(10),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(if (hasImage) image.bottom else titleText.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                if (hasAction) {
                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .constrainAs(actionButton) {
                                top.linkTo(text.bottom, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        contentPadding = PaddingValues()
                    ) {
                        Text(text = "액션!")
                    }
                }

            }
        }
    }

}

@Preview(
    name = "SVInfo",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4_XL
)
@Composable
fun SVInfoPreview() {
    SVInfo.SVInfo()
}